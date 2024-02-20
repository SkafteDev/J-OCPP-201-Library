package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.RegistrationStatusEnum;
import io.nats.client.*;
import io.nats.client.impl.NatsMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.logging.Logger;

public class ChargingStationManagementServerImpl {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final String operatorId;
    private final String csmsId;
    private final String natsConnectionUrl;
    private final IMessageRoutingMap routingMap;

    private Connection natsConnection;

    public ChargingStationManagementServerImpl(String operatorId, String csmsId, String natsConnectionUrl) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.natsConnectionUrl = natsConnectionUrl;
        routingMap = new MessageRoutingMapImpl(operatorId, csmsId, "*");
    }

    public void connect() {
        Options natsOptions = Options.builder()
                .server(natsConnectionUrl)
                .connectionName(String.format("CSMS %s %s", operatorId, csmsId))
                .connectionTimeout(Duration.ofMinutes(2))
                .connectionListener((connection, eventType) -> {
                    logger.info(String.format("NATS.io connection event: %s%n", eventType));
                })
                .build();

        try {
            natsConnection = Nats.connect(natsOptions);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    public void serve() {
        addBootNotificationDispatcher(natsConnection);
    }

    private void addBootNotificationDispatcher(Connection natsConnection) {
        Dispatcher dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String replyTo = natsMsg.getReplyTo();

            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(jsonPayload);

            try {
                ICallMessage<BootNotificationRequest> callMessage = CallMessageDeserializer.deserialize(jsonPayload, BootNotificationRequest.class);

                BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                        .withStatus(RegistrationStatusEnum.ACCEPTED)
                        .withCurrentTime(new Date())
                        .withInterval((int) Duration.ofMinutes(2).toSeconds())
                        .build();

                ICallResultMessage<BootNotificationResponse> callResultMessage =
                        CallResultMessageImpl.<BootNotificationResponse>newBuilder()
                                .withMessageId(callMessage.getMessageId()) // NB! Important to reuse the message ID for traceability
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
        });
        dispatcher.subscribe(routingMap.getRoute(OCPPMessageType.BootNotificationRequest));
    }
}
