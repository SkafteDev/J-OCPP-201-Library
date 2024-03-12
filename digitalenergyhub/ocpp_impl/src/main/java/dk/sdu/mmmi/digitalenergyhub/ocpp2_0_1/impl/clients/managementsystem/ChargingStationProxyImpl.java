package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.IChargingStationProxy;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.OCPPRequester;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.Headers;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ChargingStationProxyImpl implements IChargingStationProxy {
    private final Logger logger = Logger.getLogger(ChargingStationProxyImpl.class.getName());
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;

    public ChargingStationProxyImpl(Connection natsConnection, IMessageRouteResolver routeResolver) {
        this.natsConnection = natsConnection;
        this.routeResolver = routeResolver;
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
        var requester = new OCPPRequester<>(SetChargingProfileRequest.class, SetChargingProfileResponse.class);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.SetChargingProfileRequest);
        String responseSubject = routeResolver.getRoute(OCPPMessageType.SetChargingProfileResponse);

        ICallResultMessage<SetChargingProfileResponse> response = requester.request(request, requestSubject, responseSubject, natsConnection);

        return response;
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
