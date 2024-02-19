package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning.IChargingStationProvisioningClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.MessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ChargingStationProvisioningClientImpl implements IChargingStationProvisioningClientApi {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Connection natsConnection;
    private final Duration defaultTimeout = Duration.ofSeconds(30);

    private final MessageRoutingMap messageRoutingMap;

    public ChargingStationProvisioningClientImpl(Connection natsConnection, MessageRoutingMap routingMap) {
        this.natsConnection = natsConnection;
        this.messageRoutingMap = routingMap;
    }

    @Override
    public ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req) {
        String subject = messageRoutingMap.getRoute(MessageRoutingMap.OCPPMessageType.BootNotificationRequest);
        String replyTo = messageRoutingMap.getRoute(MessageRoutingMap.OCPPMessageType.BootNotificationResponse);

        String jsonPayload = null;

        try {
            jsonPayload = CallMessageSerializer.serialize(req);
        } catch (JsonProcessingException e) {
            // TODO: Handle if the serialization fails.
            logger.severe(e.getMessage());
            return null;
        }

        Message natsRequest = NatsMessage.builder()
                .subject(subject)
                .replyTo(replyTo)
                .data(jsonPayload, StandardCharsets.UTF_8)
                .build();

        try {
            CompletableFuture<Message> natsResponse = natsConnection.requestWithTimeout(natsRequest, defaultTimeout);
            Message message = natsResponse.get();

            // TODO: Handle the deserialization of the CallResult
            String rawData = new String (message.getData(), StandardCharsets.UTF_8);

            ICallResultMessage<String> ocppResult = CallResultMessageDeserializer.deserialize(rawData, String.class);
        } catch (InterruptedException | ExecutionException e) {
            // TODO: Handle if the CompletableFuture is interrupted or fails.
            logger.severe(e.getMessage());
            return null;
        } catch (JsonProcessingException e) {
            // TODO: Handle if the deserialization fails for CallResult, try and deserialize to CallError
            logger.severe(e.getMessage());
            return null;
        } catch (CancellationException e) {
            // TODO: Handle if the CompletableFuture got cancelled. This can happen if there are no subscribers to
            //  handle the request.
            logger.severe("The request got cancelled. This exception may happen if there are no subscribers to handle" +
                    " the request.");
            return null;
        }

        return null;
    }

    @Override
    public ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public MessageRoutingMap getMessageRoutingMap() {
        return null;
    }
}
