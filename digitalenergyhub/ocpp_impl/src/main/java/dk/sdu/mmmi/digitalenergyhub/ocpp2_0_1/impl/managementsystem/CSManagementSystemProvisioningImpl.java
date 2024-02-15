package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.managementsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.provisioning.ICSManagementSystemProvisioning;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.StatusNotificationResponse;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class CSManagementSystemProvisioningImpl implements ICSManagementSystemProvisioning {

    private final Connection natsConnection;
    private Duration defaultTimeout = Duration.ofSeconds(30);

    public CSManagementSystemProvisioningImpl(@Nonnull Connection natsConnection) {
        this.natsConnection = natsConnection;
    }

    @Override
    public ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req) {
        String subject = "operators.clever.csms.clevercsms.requests.bootnotifications";
        String replyTo = "operators.clever.csms.clevercsms.responses.bootnotifications";

        String jsonPayload = null;

        try {
            jsonPayload = CallMessageSerializer.serialize(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Message natsRequest = NatsMessage.builder()
                .subject(subject)
                .replyTo(replyTo)
                .data(jsonPayload, StandardCharsets.UTF_8)
                .build();

        CompletableFuture<Message> natsResponse = natsConnection.requestWithTimeout(natsRequest, defaultTimeout);

        //natsResponse.thenApply(() -> System.out.println());

        return null;
    }

    @Override
    public ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req) {
        return null;
    }
}
