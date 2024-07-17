package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.managementsystem;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.managementsystem.IChargingStationProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPRequester;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import io.nats.client.Connection;

import java.util.logging.Logger;

public class ChargingStationProxyImpl implements IChargingStationProxy {
    private final Logger logger = Logger.getLogger(ChargingStationProxyImpl.class.getName());
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;

    public ChargingStationProxyImpl(Connection natsConnection, IMessageRouteResolver routeResolver) {
        this.natsConnection = natsConnection;
        this.routeResolver = routeResolver;
    }

    private <TRequest, TResponse> ICallResult<TResponse> sendRequest(ICall<TRequest> req, Class<TRequest> requestClass, Class<TResponse> responseClass) {
        var requester = new OCPPRequester<>(requestClass, responseClass);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.valueOf(requestClass.getSimpleName()));
        String responseSubject = routeResolver.getRoute(OCPPMessageType.valueOf(responseClass.getSimpleName()));

        try {
            ICallResult<TResponse> response = requester.request(req, requestSubject, responseSubject, natsConnection);
            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ICallResult<CancelReservationResponse> sendCancelReservationRequest(ICall<CancelReservationRequest> request) {
        return sendRequest(request, CancelReservationRequest.class, CancelReservationResponse.class);
    }

    @Override
    public ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(ICall<CertificateSignedRequest> request) {
        return sendRequest(request, CertificateSignedRequest.class, CertificateSignedResponse.class);
    }

    @Override
    public ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(ICall<ChangeAvailabilityRequest> request) {
        return sendRequest(request, ChangeAvailabilityRequest.class, ChangeAvailabilityResponse.class);
    }

    @Override
    public ICallResult<ClearCacheResponse> sendClearCacheRequest(ICall<ClearCacheRequest> request) {
        return sendRequest(request, ClearCacheRequest.class, ClearCacheResponse.class);
    }

    @Override
    public ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(ICall<ClearChargingProfileRequest> request) {
        return sendRequest(request, ClearChargingProfileRequest.class, ClearChargingProfileResponse.class);
    }

    @Override
    public ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(ICall<ClearDisplayMessageRequest> request) {
        return sendRequest(request, ClearDisplayMessageRequest.class, ClearDisplayMessageResponse.class);
    }

    @Override
    public ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(ICall<ClearVariableMonitoringRequest> request) {
        return sendRequest(request, ClearVariableMonitoringRequest.class, ClearVariableMonitoringResponse.class);
    }

    @Override
    public ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(ICall<CostUpdatedRequest> request) {
        return sendRequest(request, CostUpdatedRequest.class, CostUpdatedResponse.class);
    }

    @Override
    public ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(ICall<CustomerInformationRequest> request) {
        return sendRequest(request, CustomerInformationRequest.class, CustomerInformationResponse.class);
    }

    @Override
    public ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(ICall<DeleteCertificateRequest> request) {
        return sendRequest(request, DeleteCertificateRequest.class, DeleteCertificateResponse.class);
    }

    @Override
    public ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(ICall<GetBaseReportRequest> request) {
        return sendRequest(request, GetBaseReportRequest.class, GetBaseReportResponse.class);
    }

    @Override
    public ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(ICall<GetChargingProfilesRequest> request) {
        return sendRequest(request, GetChargingProfilesRequest.class, GetChargingProfilesResponse.class);
    }

    @Override
    public ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(ICall<GetCompositeScheduleRequest> request) {
        return sendRequest(request, GetCompositeScheduleRequest.class, GetCompositeScheduleResponse.class);
    }

    @Override
    public ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(ICall<GetDisplayMessagesRequest> request) {
        return sendRequest(request, GetDisplayMessagesRequest.class, GetDisplayMessagesResponse.class);
    }

    @Override
    public ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(ICall<GetInstalledCertificateIdsRequest> request) {
        return sendRequest(request, GetInstalledCertificateIdsRequest.class, GetInstalledCertificateIdsResponse.class);
    }

    @Override
    public ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(ICall<GetLocalListVersionRequest> request) {
        return sendRequest(request, GetLocalListVersionRequest.class, GetLocalListVersionResponse.class);
    }

    @Override
    public ICallResult<GetLogResponse> sendGetLogRequest(ICall<GetLogRequest> request) {
        return sendRequest(request, GetLogRequest.class, GetLogResponse.class);
    }

    @Override
    public ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(ICall<GetMonitoringReportRequest> request) {
        return sendRequest(request, GetMonitoringReportRequest.class, GetMonitoringReportResponse.class);
    }

    @Override
    public ICallResult<GetReportResponse> sendGetReportRequest(ICall<GetReportRequest> request) {
        return sendRequest(request, GetReportRequest.class, GetReportResponse.class);
    }

    @Override
    public ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(ICall<GetTransactionStatusRequest> request) {
        return sendRequest(request, GetTransactionStatusRequest.class, GetTransactionStatusResponse.class);
    }

    @Override
    public ICallResult<GetVariablesResponse> sendGetVariablesRequest(ICall<GetVariablesRequest> request) {
        return sendRequest(request, GetVariablesRequest.class, GetVariablesResponse.class);
    }

    @Override
    public ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(ICall<InstallCertificateRequest> request) {
        return sendRequest(request, InstallCertificateRequest.class, InstallCertificateResponse.class);
    }

    @Override
    public ICallResult<MeterValuesResponse> sendMeterValuesRequest(ICall<MeterValuesRequest> request) {
        return sendRequest(request, MeterValuesRequest.class, MeterValuesResponse.class);
    }

    @Override
    public ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(ICall<RequestStartTransactionRequest> request) {
        return sendRequest(request, RequestStartTransactionRequest.class, RequestStartTransactionResponse.class);
    }

    @Override
    public ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(ICall<RequestStopTransactionRequest> request) {
        return sendRequest(request, RequestStopTransactionRequest.class, RequestStopTransactionResponse.class);
    }

    @Override
    public ICallResult<ReserveNowResponse> sendReserveNowRequest(ICall<ReserveNowRequest> request) {
        return sendRequest(request, ReserveNowRequest.class, ReserveNowResponse.class);
    }

    @Override
    public ICallResult<ResetResponse> sendResetRequest(ICall<ResetRequest> request) {
        return sendRequest(request, ResetRequest.class, ResetResponse.class);
    }

    @Override
    public ICallResult<SendLocalListResponse> sendSendLocalListRequest(ICall<SendLocalListRequest> request) {
        return sendRequest(request, SendLocalListRequest.class, SendLocalListResponse.class);
    }

    @Override
    public ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(ICall<SetChargingProfileRequest> request) {
        return sendRequest(request, SetChargingProfileRequest.class, SetChargingProfileResponse.class);
    }

    @Override
    public ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(ICall<SetDisplayMessageRequest> request) {
        return sendRequest(request, SetDisplayMessageRequest.class, SetDisplayMessageResponse.class);
    }

    @Override
    public ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(ICall<SetMonitoringBaseRequest> request) {
        return sendRequest(request, SetMonitoringBaseRequest.class, SetMonitoringBaseResponse.class);
    }

    @Override
    public ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(ICall<SetMonitoringLevelRequest> request) {
        return sendRequest(request, SetMonitoringLevelRequest.class, SetMonitoringLevelResponse.class);
    }

    @Override
    public ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICall<SetNetworkProfileRequest> request) {
        return sendRequest(request, SetNetworkProfileRequest.class, SetNetworkProfileResponse.class);
    }

    @Override
    public ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(ICall<SetVariableMonitoringRequest> request) {
        return sendRequest(request, SetVariableMonitoringRequest.class, SetVariableMonitoringResponse.class);
    }

    @Override
    public ICallResult<SetVariablesResponse> sendSetVariablesRequest(ICall<SetVariablesRequest> request) {
        return sendRequest(request, SetVariablesRequest.class, SetVariablesResponse.class);
    }

    @Override
    public ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(ICall<TriggerMessageRequest> request) {
        return sendRequest(request, TriggerMessageRequest.class, TriggerMessageResponse.class);
    }

    @Override
    public ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(ICall<UnlockConnectorRequest> request) {
        return sendRequest(request, UnlockConnectorRequest.class, UnlockConnectorResponse.class);
    }

    @Override
    public ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(ICall<UnpublishFirmwareRequest> request) {
        return sendRequest(request, UnpublishFirmwareRequest.class, UnpublishFirmwareResponse.class);
    }

    @Override
    public ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(ICall<UpdateFirmwareRequest> request) {
        return sendRequest(request, UpdateFirmwareRequest.class, UpdateFirmwareResponse.class);
    }
}
