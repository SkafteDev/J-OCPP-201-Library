package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.IChargingStationProxy;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.IOCPPServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.managementsystem.IChargingStationManagementServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem.ChargingStationProxyImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRouteResolverImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.OCPPServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.OCPPRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class ChargingStationManagementServerImpl implements IChargingStationManagementServer {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final String operatorId;
    private final String csmsId;
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;
    private final Map<String, ChargingStationDeviceModel> chargingStationRegistry;
    private final IOCPPServer ocppServer;

    public ChargingStationManagementServerImpl(String operatorId, String csmsId, Connection natsConnection) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.natsConnection = natsConnection;
        this.routeResolver = new MessageRouteResolverImpl(operatorId, csmsId, "*");
        this.chargingStationRegistry = new HashMap<>();

        this.ocppServer = new OCPPServerImpl(natsConnection, routeResolver);
    }

    @Override
    public void serve() {
        // TODO: Add handlers for all incoming message types.
        addBootNotificationHandler();
        addStatusNotificationHandler();
        addHeartbeatHandler();
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

            ICallMessage<SetChargingProfileRequest> call = CallMessageImpl.<SetChargingProfileRequest>newBuilder()
                    .asAction(OCPPMessageType.SetChargingProfileRequest.getValue())
                    .withMessageId(UUID.randomUUID().toString())
                    .withPayLoad(payload)
                    .build();

            try {
                IMessageRouteResolver routeResolver = new MessageRouteResolverImpl(operatorId, csmsId, csId);
                IChargingStationProxy csProxy = new ChargingStationProxyImpl(natsConnection, routeResolver);
                ICallResultMessage<SetChargingProfileResponse> callResult = csProxy.sendSetChargingProfileRequest(call);

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
                                        .withStartSchedule(DateUtil.of(2024, 1, 20, 0, 0, 0))
                                        .withChargingRateUnit(ChargingRateUnitEnum.W)
                                        /*
                                         * From this point is the limit for each hour in the day
                                         */
                                        .withChargingSchedulePeriod(List.of(
                                                // From 00:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(0 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 01:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(1 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 02:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(2 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 03:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(3 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 04:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(4 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 05:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(5 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 06:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(6 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 07:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(7 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 08:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(8 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 09:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(9 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 10:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(10 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 11:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(11 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 12:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(12 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 13:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(13 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 14:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(14 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 15:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(15 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 16:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(16 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 17:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(17 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 18:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(18 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 19:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(19 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 20:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(20 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 21:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(21 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 22:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(22 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 23:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(23 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build()
                                        )).build()
                        ))
                .build();
    }

    private void addBootNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.BootNotificationRequest,
                new OCPPRequestHandler<>(BootNotificationRequest.class, BootNotificationResponse.class) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.BootNotificationRequest);
                    }

                    @Override
                    public ICallResultMessage<BootNotificationResponse> handle(ICallMessage<BootNotificationRequest> message, String subject) {
                        // Register the charging station within the registry.
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        // Register the charging station within the registry.
                        if (!chargingStationRegistry.containsKey(csId)) {
                            ChargingStationDeviceModel csdm = new ChargingStationDeviceModel(csId,
                                    operatorId,
                                    csmsId,
                                    message.getPayload().getChargingStation());
                            csdm.setRegistrationStatus(RegistrationStatusEnum.ACCEPTED);
                            chargingStationRegistry.put(csId, csdm);
                            logger.info(String.format("Registered Charging Station: %s", csId));
                            return acceptBootNotificationRequest(message.getMessageId());
                        } else {
                            logger.info(String.format("Rejected Charging Station: %s because it is already registered.", csId));
                            return rejectBootNotificationRequest(message.getMessageId());
                        }
                    }
                });
    }

    private ICallResultMessage<BootNotificationResponse> acceptBootNotificationRequest(String responseMsgId) {
        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(new Date())
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResultMessage<BootNotificationResponse> callResultMessage =
                CallResultMessageImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }

    private ICallResultMessage<BootNotificationResponse> rejectBootNotificationRequest(String responseMsgId) {
        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.REJECTED)
                .withCurrentTime(new Date())
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResultMessage<BootNotificationResponse> callResultMessage =
                CallResultMessageImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }


    private void addStatusNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.StatusNotificationRequest,
                new OCPPRequestHandler<>(StatusNotificationRequest.class, StatusNotificationResponse.class) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.StatusNotificationRequest);
                    }

                    @Override
                    public ICallResultMessage<StatusNotificationResponse> handle(ICallMessage<StatusNotificationRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        StatusNotificationRequest statusNotificationRequest = message.getPayload();

                        logger.info(String.format("Processing '%s' payload='%s' for ChargingStation='%s'",
                                OCPPMessageType.StatusNotificationRequest,
                                statusNotificationRequest.toString(),
                                csId));

                        return acceptStatusNotificationRequest(message.getMessageId());
                    }
                });
    }

    private ICallResultMessage<StatusNotificationResponse> acceptStatusNotificationRequest(String responseMsgId) {
        StatusNotificationResponse statusNotificationResponse = StatusNotificationResponse.builder()
                // No fields required as specified in OCPP 2.0.1.
                .build();

        ICallResultMessage<StatusNotificationResponse> callResultMessage =
                CallResultMessageImpl.<StatusNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(statusNotificationResponse)
                        .build();

        return callResultMessage;
    }

    private void addHeartbeatHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.HeartbeatRequest,
                new OCPPRequestHandler<>(HeartbeatRequest.class, HeartbeatResponse.class) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.HeartbeatRequest);
                    }

                    @Override
                    public ICallResultMessage<HeartbeatResponse> handle(ICallMessage<HeartbeatRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        HeartbeatRequest heartbeatRequest = message.getPayload();

                        logger.info(String.format("Processing '%s' payload='%s' for ChargingStation='%s'",
                                OCPPMessageType.HeartbeatRequest,
                                heartbeatRequest.toString(),
                                csId));

                        return acceptHeartbeatRequest(message.getMessageId());
                    }
                });
    }

    private ICallResultMessage<HeartbeatResponse> acceptHeartbeatRequest(String responseMsgId) {
        HeartbeatResponse response = HeartbeatResponse.builder()
                .withCurrentTime(DateUtil.now())
                .build();

        ICallResultMessage<HeartbeatResponse> callResultMessage =
                CallResultMessageImpl.<HeartbeatResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(response)
                        .build();

        return callResultMessage;
    }
}
