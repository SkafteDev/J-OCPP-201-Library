package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.ICsmsProxy;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
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
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class CsmsProxyImpl implements ICsmsProxy {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Connection natsConnection;
    private final Duration defaultTimeout = Duration.ofSeconds(30);

    private final IMessageRouteResolver routeResolver;

    public CsmsProxyImpl(Connection natsConnection, IMessageRouteResolver routingMap) {
        this.natsConnection = natsConnection;
        this.routeResolver = routingMap;
    }

    @Override
    public ICallResultMessage<AuthorizeResponse> sendAuthorizeRequest(ICallMessage<AuthorizeRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req) {
        String subject = routeResolver.getRoute(OCPPMessageType.BootNotificationRequest);

        String jsonPayloadRequest = null;

        try {
            jsonPayloadRequest = CallMessageSerializer.serialize(req);
        } catch (JsonProcessingException e) {
            // TODO: Handle if the serialization fails.
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        Message natsRequest = NatsMessage.builder()
                .subject(subject)
                .data(jsonPayloadRequest, StandardCharsets.UTF_8)
                .build();

        try {
            CompletableFuture<Message> natsResponse = natsConnection.requestWithTimeout(natsRequest, defaultTimeout);
            Message message = natsResponse.get();

            String jsonPayloadResponse = new String (message.getData(), StandardCharsets.UTF_8);

            ICallResultMessage<BootNotificationResponse> callResult = CallResultMessageDeserializer.deserialize(jsonPayloadResponse,
                    BootNotificationResponse.class);

            return callResult;
        } catch (InterruptedException | ExecutionException e) {
            // TODO: Handle if the CompletableFuture is interrupted or fails.
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (JsonProcessingException e) {
            // TODO: Handle if the deserialization fails for CallResult, try and deserialize to CallError
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (CancellationException e) {
            // TODO: Handle if the CompletableFuture got cancelled. This can happen if there are no subscribers to
            //  handle the request.
            logger.severe("The request got cancelled. This exception may happen if there are no subscribers to handle" +
                    " the request.");
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ICallResultMessage<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(ICallMessage<ClearedChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(ICallMessage<FirmwareStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(ICallMessage<Get15118EVCertificateRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<GetCertificateStatusResponse> sendGetCertificateStatusRequest(ICallMessage<GetCertificateStatusRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req) {
        String subject = routeResolver.getRoute(OCPPMessageType.StatusNotificationRequest);

        String jsonPayloadRequest = null;

        try {
            jsonPayloadRequest = CallMessageSerializer.serialize(req);
        } catch (JsonProcessingException e) {
            // TODO: Handle if the serialization fails.
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

        Message natsRequest = NatsMessage.builder()
                .subject(subject)
                .data(jsonPayloadRequest, StandardCharsets.UTF_8)
                .build();

        try {
            CompletableFuture<Message> natsResponse = natsConnection.requestWithTimeout(natsRequest, defaultTimeout);
            Message message = natsResponse.get();

            String jsonPayloadResponse = new String (message.getData(), StandardCharsets.UTF_8);

            ICallResultMessage<StatusNotificationResponse> callResult =
                    CallResultMessageDeserializer.deserialize(jsonPayloadResponse,
                            StatusNotificationResponse.class);

            return callResult;
        } catch (InterruptedException | ExecutionException e) {
            // TODO: Handle if the CompletableFuture is interrupted or fails.
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (JsonProcessingException e) {
            // TODO: Handle if the deserialization fails for CallResult, try and deserialize to CallError
            logger.severe(e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (CancellationException e) {
            // TODO: Handle if the CompletableFuture got cancelled. This can happen if there are no subscribers to
            //  handle the request.
            logger.severe("The request got cancelled. This exception may happen if there are no subscribers to handle" +
                    " the request.");
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ICallResultMessage<TransactionEventResponse> sendTransactionEventRequest(ICallMessage<TransactionEventRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public ICallResultMessage<LogStatusNotificationResponse> sendLogStatusNotificationRequest(ICallMessage<LogStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(ICallMessage<NotifyChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(ICallMessage<NotifyCustomerInformationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(ICallMessage<NotifyDisplayMessagesRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(ICallMessage<NotifyEVChargingNeedsRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(ICallMessage<NotifyEVChargingScheduleRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEventResponse> sendNotifyEventRequest(ICallMessage<NotifyEventRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(ICallMessage<NotifyMonitoringReportRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public ICallResultMessage<PublishFirmwareResponse> sendPublishFirmwareRequest(ICallMessage<PublishFirmwareRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(ICallMessage<ReportChargingProfilesRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(ICallMessage<ReservationStatusUpdateRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(ICallMessage<SecurityEventNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<SignCertificateResponse> sendSignCertificateRequest(ICallMessage<SignCertificateRequest> req) {
        return null;
    }

    @Override
    public IMessageRouteResolver getRouteResolver() {
        return this.routeResolver;
    }
}
