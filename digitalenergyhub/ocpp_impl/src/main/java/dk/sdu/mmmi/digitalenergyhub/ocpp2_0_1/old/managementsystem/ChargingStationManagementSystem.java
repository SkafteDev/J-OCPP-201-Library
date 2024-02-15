package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.managementsystem;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.nats.NatsClient;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.messagetypes.OCPPMessageToSubjectMapping.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.managementsystem.handlers.SetChargingProfileResponseHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.managementsystem.handlers.StatusNotificationRequestHandler;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.datatypes.ChargingProfileType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.datatypes.ChargingSchedulePeriodType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.datatypes.ChargingScheduleType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.enumerations.ChargingProfileKindEnumType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.enumerations.ChargingProfilePurposeEnumType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.enumerations.ChargingRateUnitEnumType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.SetChargingProfileRequest;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChargingStationManagementSystem {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final String operatorId;
    private final String csmsId;
    private final String natsConnectionString;
    private final NatsClient natsClient;

    private final Map<String, IChargingStationServer<Message>> chargingStationProxies = new HashMap<>();

    public ChargingStationManagementSystem(String operatorId, String csmsId, String natsConnectionString) {
        this.operatorId = sanitise(operatorId);
        this.csmsId = sanitise(csmsId);
        this.natsConnectionString = natsConnectionString;
        this.natsClient = new NatsClient(natsConnectionString);
    }

    private String sanitise(String in) {
        return in.replace(" ", "").toLowerCase();
    }

    public void addChargingStation(String chargingStationId) {
        chargingStationId = sanitise(chargingStationId);
        // Guard. Do not add existing charging station.
        if (chargingStationProxies.containsKey(chargingStationId)) return;

        IChargingStationServer<Message> proxyObject = getChargingStationProxyObject(chargingStationId);
        chargingStationProxies.put(chargingStationId, proxyObject);
        addMessageHandlers(proxyObject);
    }

    private IChargingStationServer<Message> getChargingStationProxyObject(String finalChargingStationId) {
        return new IChargingStationServer<Message>() {
            @Override
            public String getOperatorId() {
                return operatorId;
            }

            @Override
            public String getCsmsId() {
                return csmsId;
            }

            @Override
            public String getCsId() {
                return finalChargingStationId;
            }

            @Override
            public void addRequestHandler(OCPPMessageType ocppMessageType, IMessageHandler<Message> handler) {
                throw new UnsupportedOperationException("Not supported on proxy object.");
            }

            @Override
            public void emitMessage(OCPPMessageType ocppMessageType, Message message) {
                throw new UnsupportedOperationException("Not supported on proxy object.");
            }
        };
    }

    public void calculateAndPublishNewChargingSchedules(String chargingStationId) {
        SetChargingProfileRequest scpr = SetChargingProfileRequest.newBuilder()
                .setEvseId(0) // Apply the profile to each individual evse.
                .setChargingProfile(ChargingProfileType.newBuilder()
                        .setId(1)
                        .setStackLevel(0)
                        .setChargingProfilePurpose(ChargingProfilePurposeEnumType.ChargingStationMaxProfile)
                        .setChargingProfileKind(ChargingProfileKindEnumType.Absolute)
                        .addChargingSchedule(ChargingScheduleType.newBuilder()
                                .setId(1)
                                .setStartSchedule(Timestamp.newBuilder()
                                        .setSeconds(LocalDateTime.of(2024, 1, 20, 0, 0).toEpochSecond(ZoneOffset.UTC))
                                        .build())
                                .setChargingRateUnit(ChargingRateUnitEnumType.W)
                                /*
                                * From this point is the limit for each hour in the day
                                */
                                // From 00:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(0 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 01:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(1 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 02:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(2 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 03:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(3 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 04:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(4 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 05:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(5 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 06:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(6 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 07:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(7 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 08:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(8 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 09:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(9 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 10:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(10 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 11:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(11 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 12:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(12 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 13:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(13 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 14:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(14 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 15:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(15 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 16:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(16 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 17:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(17 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 18:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(18 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 19:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(19 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 20:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(20 * 60 * 60)
                                        .setLimit(0)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 21:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(21 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 22:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(22 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                // From 23:00
                                .addChargingSchedulePeriod(ChargingSchedulePeriodType.newBuilder()
                                        .setStartPeriod(23 * 60 * 60)
                                        .setLimit(11_000)
                                        .setNumberPhases(1)
                                        .setPhaseToUse(1)
                                        .build())
                                .build())
                        .build())
                .build();

        publishSetChargingProfileRequest(scpr, chargingStationId);
    }

    private void publishSetChargingProfileRequest(SetChargingProfileRequest request, String chargingStationId) {
        IChargingStationServer<Message> csProxy = this.chargingStationProxies.get(chargingStationId);

        String requestChannel = csProxy.getRequestChannel(OCPPMessageType.SetChargingProfileRequest);
        String responseChannel = csProxy.getResponseChannel(OCPPMessageType.SetChargingProfileResponse);

        try {
            String jsonPayload = JsonFormat.printer().print(request);

            Message natsMessage = NatsMessage.builder()
                    .subject(requestChannel)
                    .replyTo(responseChannel)
                    .data(jsonPayload.getBytes(StandardCharsets.UTF_8))
                    .build();

            this.natsClient.publish(requestChannel, natsMessage);

            LocalDateTime ldt = LocalDateTime.now();
            String logMsg = String.format("%s sent message type %s on subject %s:%n%s",
                    ldt,
                    OCPPMessageType.SetChargingProfileRequest,
                    requestChannel,
                    natsMessage);
            logger.info(logMsg);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMessageHandlers(IChargingStationServer<Message> chargingStationServerProxyObject) {
        natsClient.addSubscriber(chargingStationServerProxyObject.getEventsChannel() + "." + OCPPMessageType.StatusNotificationRequest,
                new StatusNotificationRequestHandler());
        natsClient.addSubscriber(chargingStationServerProxyObject.getResponseChannel(OCPPMessageType.SetChargingProfileResponse),
                new SetChargingProfileResponseHandler());

        // TODO: Add message handlers for each message type.
    }
}
