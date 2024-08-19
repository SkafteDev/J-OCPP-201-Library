package dk.sdu.mmmi.jocpp.application.csms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.managementsystem.ChargingStationNatsIOProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.routes.MessageRouteResolverImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPOverNatsIOService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Logger;

public class ChargingStationManagementServerImpl implements IChargingStationManagementServer, ICsmsService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final String operatorId;
    private final String csmsId;
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;
    private final Map<String, ChargingStationDeviceModel> chargingStationRegistry;

    private final Map<String, ICsmsServiceEndpoint> endpoints;

    private final IRequestHandlerRegistry ocppServer;

    public ChargingStationManagementServerImpl(String operatorId, String csmsId, Connection natsConnection) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.natsConnection = natsConnection;
        this.routeResolver = new MessageRouteResolverImpl(operatorId, csmsId, "*");
        this.chargingStationRegistry = new HashMap<>();
        this.endpoints = new HashMap<>();

        this.ocppServer = new OCPPOverNatsIOService(natsConnection, routeResolver);
    }

    @Override
    public void serve() {
        addConnectionHandler();

        // TODO: Add handlers for all incoming message types.
        addBootNotificationHandler();
        addStatusNotificationHandler();
        addHeartbeatHandler();
    }

    private void addConnectionHandler() {
        // Connect handler
        Dispatcher dispatcher = natsConnection.createDispatcher(handshakeReq -> {
            String replyTo = handshakeReq.getReplyTo();

            // Deserialize the handshake
            String json = new String(handshakeReq.getData(), StandardCharsets.UTF_8);

            ObjectMapper mapper = JacksonUtil.getDefault();

            try {
                HandshakeRequest handshakeRequest = mapper.readValue(json, HandshakeRequestImpl.class);
                System.out.println(handshakeRequest);
                // TODO: Add endpoint to the internal map.bash
                ICsmsServiceEndpoint endpoint = connect(handshakeRequest);

                MessageRouteResolverImpl routeResolver = new MessageRouteResolverImpl(operatorId, csmsId, handshakeRequest.getIdentity());
                HandshakeResponseImpl accepted = HandshakeResponseImpl.HandshakeResponseImplBuilder.newHandshakeResponseImpl()
                        .withHandshakeResult("Accepted")
                        .withEndpoint(routeResolver.getRequestRoute() + ", " + routeResolver.getResponseRoute())
                        .build();

                String jsonResponse = mapper.writerFor(HandshakeResponseImpl.class).writeValueAsString(accepted);
                natsConnection.publish(replyTo, jsonResponse.getBytes(StandardCharsets.UTF_8));

            } catch (JsonProcessingException e) {
                HandshakeResponseImpl rejected = HandshakeResponseImpl.HandshakeResponseImplBuilder.newHandshakeResponseImpl()
                        .withHandshakeResult("Rejected")
                        .withDescription("An error occurred during handshake.")
                        .build();

                String jsonResponse = null;
                try {
                    jsonResponse = mapper.writerFor(HandshakeResponseImpl.class).writeValueAsString(rejected);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                natsConnection.publish(replyTo, jsonResponse.getBytes(StandardCharsets.UTF_8));
            }
        });

        // Subscribe to the connection subject to handle the handshake request.
        String operatorId = this.operatorId.replace(" ", "").toLowerCase();
        String csmsId = this.csmsId.replace(" ", "").toLowerCase();
        dispatcher.subscribe(String.format("operators.%s.csms.%s.connect", operatorId, csmsId));
    }

    @Override
    public void startSmartChargingControlLoop(Duration interval) {
        // Control loop
        while (true) {
            long startTime = System.currentTimeMillis();
            logger.info(String.format("Running Smart Charging control loop with interval=%s seconds.",
                    interval.toSeconds()));

            // Regular control flow
            logger.info("Calculating ChargingProfiles.");
            Map<String, ChargingProfile> profileMap = calculateChargingProfiles();
            logger.info("Finished calculation of ChargingProfiles.");
            emitChargingProfiles(profileMap);

            // Calculate the time spent controlling.
            Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - startTime);
            logger.info(String.format("Completed control loop in %s seconds.", elapsed.toSeconds()));

            try {
                // If we spent more time than 'allowed' during the control loop, set the interval to 0.
                Duration waitTimeBeforeNextLoop = interval.minus(elapsed).toMillis() >= 0 ? interval.minus(elapsed) : Duration.ZERO;
                logger.info(String.format("Waiting %s seconds before proceeding to next control iteration.",
                        waitTimeBeforeNextLoop.toSeconds()));
                Thread.sleep(waitTimeBeforeNextLoop.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void emitChargingProfiles(Map<String, ChargingProfile> profileMap) {
        if (profileMap.isEmpty()) return; // No need to emit anything.

        logger.info("Emitting ChargingProfiles to Charging Stations.");
        for (Map.Entry<String, ChargingProfile> entry : profileMap.entrySet()) {
            String csId = entry.getKey();
            ChargingProfile chargingProfile = entry.getValue();

            SetChargingProfileRequest payload = SetChargingProfileRequest.builder()
                    .withEvseId(0)
                    .withChargingProfile(chargingProfile)
                    .build();

            ICall<SetChargingProfileRequest> call = CallImpl.<SetChargingProfileRequest>newBuilder()
                    .asAction(OCPPMessageType.SetChargingProfileRequest.getAction())
                    .withMessageId(UUID.randomUUID().toString())
                    .withPayLoad(payload)
                    .build();

            try {
                IMessageRouteResolver routeResolver = new MessageRouteResolverImpl(operatorId, csmsId, csId);
                IChargingStationServiceEndpoint csProxy = new ChargingStationNatsIOProxy(natsConnection, routeResolver);
                ICallResult<SetChargingProfileResponse> callResult = csProxy.sendSetChargingProfileRequest(call);

                logger.info(String.format("Received response '%s' with payload %s",
                        SetChargingProfileResponse.class.getName(), callResult.getPayload().toString()));
            } catch (OCPPRequestException e) {
                logger.info(String.format("Error occurred while sending the request or receiving the response. %s",
                        e.getMessage()));
            }
        }
    }

    /**
     * Returns a map of Charging Station IDs and for each, a generated Charging Profile.
     *
     * @return
     */
    private Map<String, ChargingProfile> calculateChargingProfiles() {
        Map<String, ChargingProfile> profileMap = new HashMap<>();

        try {
            // Simulate a long-running task (calculating charging profiles)
            double taskLength = Math.random() * 30d;
            Thread.sleep((int) taskLength);

            for (ChargingStationDeviceModel csDeviceModel : chargingStationRegistry.values()) {
                ChargingProfile chargingProfile = getChargingProfile();
                profileMap.put(csDeviceModel.getCsId(), chargingProfile);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return profileMap;
    }

    private ChargingProfile getChargingProfile() {
        return ChargingProfile.builder()
                .withId(1)
                .withStackLevel(0)
                .withChargingProfilePurpose(ChargingProfilePurposeEnum.CHARGING_STATION_MAX_PROFILE)
                .withChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE)
                .withChargingSchedule(
                        List.of(
                                ChargingSchedule.builder()
                                        .withId(1)
                                        .withStartSchedule(ZonedDateTime.of(2024, 1, 20, 0, 0, 0, 0, ZoneId.systemDefault()))
                                        .withChargingRateUnit(ChargingRateUnitEnum.W)
                                        /*
                                         * From this point is the limit for each hour in the day
                                         */
                                        .withChargingSchedulePeriod(List.of(
                                                // From 00:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(0 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 01:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(1 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 02:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(2 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 03:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(3 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 04:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(4 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 05:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(5 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 06:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(6 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 07:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(7 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 08:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(8 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 09:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(9 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 10:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(10 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 11:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(11 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 12:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(12 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 13:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(13 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 14:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(14 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 15:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(15 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 16:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(16 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 17:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(17 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 18:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(18 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 19:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(19 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 20:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(20 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 21:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(21 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 22:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(22 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 23:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(23 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build()
                                        )).build()
                        ))
                .build();
    }

    private void addBootNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.BootNotificationRequest,
                new OCPPOverNatsIORequestHandler<>(BootNotificationRequest.class, BootNotificationResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.BootNotificationRequest);
                    }

                    @Override
                    public ICallResult<BootNotificationResponse> handle(ICall<BootNotificationRequest> message, String subject) {
                        // Register the charging station within the registry.
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        // Register the charging station within the registry.
                        if (endpoints.containsKey(csId)) {
                            logger.info(String.format("Dispatching BootNotificationRequest: %s", csId));
                            return endpoints.get(csId).sendBootNotificationRequest(message);
                        } else {
                            logger.info(String.format("Dispatch error. CS '%s' is not connected.", csId));
                            ICallError callError = CallErrorImpl.newCallErrorBuilder()
                                    .withMessageId(message.getMessageId())
                                    .withErrorCode(ErrorCode.GENERIC_ERROR)
                                    .withErrorDescription("Not connected.")
                                    .build();

                            return callError;
                        }
                    }
                });
    }

    private void addStatusNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.StatusNotificationRequest,
                new OCPPOverNatsIORequestHandler<>(StatusNotificationRequest.class, StatusNotificationResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.StatusNotificationRequest);
                    }

                    @Override
                    public ICallResult<StatusNotificationResponse> handle(ICall<StatusNotificationRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        if (endpoints.containsKey(csId)) {
                            logger.info(String.format("Dispatching StatusNotificationRequest: %s", csId));
                            return endpoints.get(csId).sendStatusNotificationRequest(message);
                        } else {
                            logger.info(String.format("Dispatch error. CS '%s' is not connected.", csId));
                            ICallError callError = CallErrorImpl.newCallErrorBuilder()
                                    .withMessageId(message.getMessageId())
                                    .withErrorCode(ErrorCode.GENERIC_ERROR)
                                    .withErrorDescription("Not connected.")
                                    .build();

                            return callError;
                        }
                    }
                });
    }

    private void addHeartbeatHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.HeartbeatRequest,
                new OCPPOverNatsIORequestHandler<>(HeartbeatRequest.class, HeartbeatResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.HeartbeatRequest);
                    }

                    @Override
                    public ICallResult<HeartbeatResponse> handle(ICall<HeartbeatRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        if (endpoints.containsKey(csId)) {
                            logger.info(String.format("Dispatching HeartbeatRequest: %s", csId));
                            return endpoints.get(csId).sendHeartbeatRequest(message);
                        } else {
                            logger.info(String.format("Dispatch error. CS '%s' is not connected.", csId));
                            ICallError callError = CallErrorImpl.newCallErrorBuilder()
                                    .withMessageId(message.getMessageId())
                                    .withErrorCode(ErrorCode.GENERIC_ERROR)
                                    .withErrorDescription("Not connected.")
                                    .build();

                            return callError;
                        }
                    }
                });
    }

    @Override
    public ICsmsServiceEndpoint connect(HandshakeRequest handshakeRequest) {
        // TODO: Add validation of whether to accept/reject the incoming connection.
        ICsmsServiceEndpoint csmsServiceEndpoint = new CsmsServiceEndpoint(handshakeRequest);
        this.endpoints.put(handshakeRequest.getIdentity(), csmsServiceEndpoint);

        return csmsServiceEndpoint;
    }
}
