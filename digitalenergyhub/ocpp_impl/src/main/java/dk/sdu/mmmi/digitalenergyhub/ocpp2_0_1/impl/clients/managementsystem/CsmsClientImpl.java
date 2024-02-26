package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.ICsmsClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class CsmsClientImpl implements ICsmsClientApi {
    private final Logger logger = Logger.getLogger(CsmsClientImpl.class.getName());
    private final Connection natsConnection;
    private final IMessageRoutingMap routingMap;

    public CsmsClientImpl(Connection natsConnection, IMessageRoutingMap routingMap) {
        this.natsConnection = natsConnection;
        this.routingMap = routingMap;
    }

    @Override
    public ICallResultMessage<CancelReservationResponse> sendCancelReservationRequest(ICallMessage<CancelReservationRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<CertificateSignedResponse> sendCertificateSignedRequest(ICallMessage<CertificateSignedRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(ICallMessage<ChangeAvailabilityRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ClearCacheResponse> sendClearCacheRequest(ICallMessage<ClearCacheRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ClearChargingProfileResponse> sendClearChargingProfileRequest(ICallMessage<ClearChargingProfileRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(ICallMessage<ClearDisplayMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(ICallMessage<ClearVariableMonitoringRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<CostUpdatedResponse> sendCostUpdatedRequest(ICallMessage<CostUpdatedRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<CustomerInformationResponse> sendCustomerInformationRequest(ICallMessage<CustomerInformationRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<DeleteCertificateResponse> sendDeleteCertificateRequest(ICallMessage<DeleteCertificateRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetBaseReportResponse> sendGetBaseReportRequest(ICallMessage<GetBaseReportRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetChargingProfilesResponse> sendGetChargingProfilesRequest(ICallMessage<GetChargingProfilesRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(ICallMessage<GetCompositeScheduleRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(ICallMessage<GetDisplayMessagesRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(ICallMessage<GetInstalledCertificateIdsRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetLocalListVersionResponse> sendGetLocalListVersionRequest(ICallMessage<GetLocalListVersionRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetLogResponse> sendGetLogRequest(ICallMessage<GetLogRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetMonitoringReportResponse> sendGetMonitoringReportRequest(ICallMessage<GetMonitoringReportRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetReportResponse> sendGetReportRequest(ICallMessage<GetReportRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetTransactionStatusResponse> sendGetTransactionStatusRequest(ICallMessage<GetTransactionStatusRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<GetVariablesResponse> sendGetVariablesRequest(ICallMessage<GetVariablesRequest> req) {
        // ICallMessage<GetVariablesRequest> callMsg = CallMessageImpl.<GetVariablesRequest>newBuilder().build();

        // Starting here:

        // Serialize to JSON format.
        // Optionally, validate format before sending.

        // Send the msg to the NATS.io broker as a request with a pre-programmed timeout.

        // Await a response from NATS.io.

        // Deserialize the response from NATS.io.

        // Return the response...
        return null;
    }

    @Override
    public ICallResultMessage<InstallCertificateResponse> sendInstallCertificateRequest(ICallMessage<InstallCertificateRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<MeterValuesResponse> sendMeterValuesRequest(ICallMessage<MeterValuesRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<RequestStartTransactionResponse> sendRequestStartTransactionRequest(ICallMessage<RequestStartTransactionRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<RequestStopTransactionResponse> sendRequestStopTransactionRequest(ICallMessage<RequestStopTransactionRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ReserveNowResponse> sendReserveNowRequest(ICallMessage<ReserveNowRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<ResetResponse> sendResetRequest(ICallMessage<ResetRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SendLocalListResponse> sendSendLocalListRequest(ICallMessage<SendLocalListRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetChargingProfileResponse> sendSetChargingProfileRequest(ICallMessage<SetChargingProfileRequest> request) {
        try {
            String jsonRequestPayload = CallMessageSerializer.serialize(request);
            String requestSubject = routingMap.getRoute(OCPPMessageType.SetChargingProfileRequest);
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

            return callResult;

        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ICallResultMessage<SetDisplayMessageResponse> sendSetDisplayMessageRequest(ICallMessage<SetDisplayMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(ICallMessage<SetMonitoringBaseRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(ICallMessage<SetMonitoringLevelRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICallMessage<SetNetworkProfileRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(ICallMessage<SetVariableMonitoringRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<SetVariablesResponse> sendSetVariablesRequest(ICallMessage<SetVariablesRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<TriggerMessageResponse> sendTriggerMessageRequest(ICallMessage<TriggerMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<UnlockConnectorResponse> sendUnlockConnectorRequest(ICallMessage<UnlockConnectorRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(ICallMessage<UnpublishFirmwareRequest> request) {
        return null;
    }

    @Override
    public ICallResultMessage<UpdateFirmwareResponse> sendUpdateFirmwareRequest(ICallMessage<UpdateFirmwareRequest> request) {
        return null;
    }
}
