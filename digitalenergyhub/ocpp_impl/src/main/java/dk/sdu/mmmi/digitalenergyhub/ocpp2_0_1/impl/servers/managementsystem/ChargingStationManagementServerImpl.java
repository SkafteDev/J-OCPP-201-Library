package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.managementsystem.IChargingStationManagementServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ChargingStationManagementServerImpl implements IChargingStationManagementServer {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final String operatorId;
    private final String csmsId;
    private final IMessageRoutingMap routingMap;

    private final Map<String, ChargingStationDeviceModel> chargingStationRegistry;

    private final Connection natsConnection;

    public ChargingStationManagementServerImpl(String operatorId, String csmsId, Connection natsConnection) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.natsConnection = natsConnection;
        this.routingMap = new MessageRoutingMapImpl(operatorId, csmsId, "*");
        this.chargingStationRegistry = new HashMap<>();
    }

    @Override
    public void connect() {
    }

    @Override
    public void serve() {
        // TODO: Add dispatchers for all incoming message types.
        addBootNotificationDispatcher(natsConnection);
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
                Duration waitTimeBeforeNextLoop = interval.minus(elapsed);
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
                String jsonRequestPayload = CallMessageSerializer.serialize(call);
                String requestSubject = routingMap.getRoute(OCPPMessageType.SetChargingProfileRequest).replace("*", csId);
                Message natsMessage = NatsMessage.builder()
                        .subject(requestSubject)
                        .data(jsonRequestPayload, StandardCharsets.UTF_8)
                        .build();

                logger.info(String.format("Sending request '%s' with payload %s on subject %s",
                        SetChargingProfileRequest.class.getName(), jsonRequestPayload, requestSubject));
                CompletableFuture<Message> response = natsConnection.requestWithTimeout(natsMessage, Duration.ofSeconds(30));

                // Blocks until message is received.
                // TODO: Join all futures and get the result at a later point in time, to avoid blocking.
                Message message = response.get();
                String jsonResponsePayload = new String(message.getData(), StandardCharsets.UTF_8);
                ICallResultMessage<SetChargingProfileResponse> callResult =
                        CallResultMessageDeserializer.deserialize(jsonResponsePayload, SetChargingProfileResponse.class);

                logger.info(String.format("Received response '%s' with payload %s on subject %s",
                        SetChargingProfileResponse.class.getName(), callResult.getPayload().toString(),
                        message.getSubject()));

            } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns a map of Charging Station IDs and for each, a generated Charging Profile.
     * @return
     */
    private Map<String, ChargingProfile> calculateChargingProfiles() {
        Map<String, ChargingProfile> profileMap = new HashMap<>();

        try {
            // Simulate a long-running task (calculating charging profiles)
            double taskLength = Math.random() * 30d;
            Thread.sleep((int)taskLength);

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

    private void addBootNotificationDispatcher(Connection natsConnection) {
        Dispatcher dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received 'BootNotificationRequest' %s on subject %s", jsonPayload, natsMsg.getSubject()));

            ICallMessage<BootNotificationRequest> callMessage;

            try {
                callMessage = CallMessageDeserializer.deserialize(jsonPayload, BootNotificationRequest.class);
            } catch (JsonProcessingException e) {
                logger.severe(e.getMessage());
                throw new RuntimeException(e);
            }

            // Register the charging station within the registry.
            String[] topicLevels = natsMsg.getSubject().split("\\.");
            String csId = topicLevels[5];
            String replyTo = natsMsg.getReplyTo();
            String responseMsgId = callMessage.getMessageId();

            // Register the charging station within the registry.
            if (!chargingStationRegistry.containsKey(csId)) {
                ChargingStationDeviceModel csdm = new ChargingStationDeviceModel(csId,
                        operatorId,
                        csmsId,
                        callMessage.getPayload().getChargingStation());
                csdm.setRegistrationStatus(RegistrationStatusEnum.ACCEPTED);
                this.chargingStationRegistry.put(csId, csdm);
                logger.info(String.format("Registered Charging Station: %s", csId));
                acceptBootNotificationRequest(natsConnection, replyTo, responseMsgId);
            } else {
                logger.info(String.format("Rejected Charging Station: %s because it is already registered.", csId));
                rejectBootNotificationRequest(natsConnection, replyTo, responseMsgId);
            }

        });
        String route = routingMap.getRoute(OCPPMessageType.BootNotificationRequest);
        logger.info(String.format("Listening for '%s' on subject '%s'",
                OCPPMessageType.BootNotificationRequest.getValue().concat("Request"), route));
        dispatcher.subscribe(route);
    }

    private void acceptBootNotificationRequest(Connection natsConnection, String replyTo, String responseMsgId) {
        try {
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

            String jsonResponse = CallResultMessageSerializer.serialize(callResultMessage);

            Message natsResponseMsg = NatsMessage.builder()
                    .subject(replyTo)
                    .data(jsonResponse)
                    .build();

            natsConnection.publish(natsResponseMsg);
            logger.info(jsonResponse);

        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void rejectBootNotificationRequest(Connection natsConnection, String replyTo, String responseMsgId) {
        try {
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

            String jsonResponse = CallResultMessageSerializer.serialize(callResultMessage);

            Message natsResponseMsg = NatsMessage.builder()
                    .subject(replyTo)
                    .data(jsonResponse)
                    .build();

            natsConnection.publish(natsResponseMsg);
            logger.info(jsonResponse);

        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
