package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.managementsystem;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IChargingStation;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPOverNatsIORequester;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;

import java.util.logging.Logger;

public class ChargingStationNatsIOProxy implements IChargingStation {
    private final Logger logger = Logger.getLogger(ChargingStationNatsIOProxy.class.getName());
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;

    public ChargingStationNatsIOProxy(Connection natsConnection, IMessageRouteResolver routeResolver) {
        this.natsConnection = natsConnection;
        this.routeResolver = routeResolver;
    }

    private <TRequest, TResponse> ICallResult<TResponse> sendRequest(ICall<TRequest> req, Class<TRequest> requestClass, Class<TResponse> responseClass) {
        var requester = new OCPPOverNatsIORequester<>(requestClass, responseClass);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.valueOf(requestClass.getSimpleName()));
        String responseSubject = routeResolver.getRoute(OCPPMessageType.valueOf(responseClass.getSimpleName()));

        try {
            ICallResult<TResponse> response = requester.request(req, requestSubject, responseSubject, natsConnection);
            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            throw new OCPPRequestException(String.format("Failed to send/receive OCPP request on subjects reqSubject='%s', " +
                    "respSubject=%s", requestSubject, responseSubject), ex);
        }
    }

    @Override
    public ICallResult<CancelReservationResponse> sendCancelReservationRequest(Headers headers, ICall<CancelReservationRequest> request) {
        return sendRequest(request, CancelReservationRequest.class, CancelReservationResponse.class);
    }

    @Override
    public ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(Headers headers, ICall<CertificateSignedRequest> request) {
        return sendRequest(request, CertificateSignedRequest.class, CertificateSignedResponse.class);
    }

    @Override
    public ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(Headers headers, ICall<ChangeAvailabilityRequest> request) {
        return sendRequest(request, ChangeAvailabilityRequest.class, ChangeAvailabilityResponse.class);
    }

    @Override
    public ICallResult<ClearCacheResponse> sendClearCacheRequest(Headers headers, ICall<ClearCacheRequest> request) {
        return sendRequest(request, ClearCacheRequest.class, ClearCacheResponse.class);
    }

    @Override
    public ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(Headers headers, ICall<ClearChargingProfileRequest> request) {
        return sendRequest(request, ClearChargingProfileRequest.class, ClearChargingProfileResponse.class);
    }

    @Override
    public ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(Headers headers, ICall<ClearDisplayMessageRequest> request) {
        return sendRequest(request, ClearDisplayMessageRequest.class, ClearDisplayMessageResponse.class);
    }

    @Override
    public ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(Headers headers, ICall<ClearVariableMonitoringRequest> request) {
        return sendRequest(request, ClearVariableMonitoringRequest.class, ClearVariableMonitoringResponse.class);
    }

    @Override
    public ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(Headers headers, ICall<CostUpdatedRequest> request) {
        return sendRequest(request, CostUpdatedRequest.class, CostUpdatedResponse.class);
    }

    @Override
    public ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(Headers headers, ICall<CustomerInformationRequest> request) {
        return sendRequest(request, CustomerInformationRequest.class, CustomerInformationResponse.class);
    }

    @Override
    public ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(Headers headers, ICall<DeleteCertificateRequest> request) {
        return sendRequest(request, DeleteCertificateRequest.class, DeleteCertificateResponse.class);
    }

    @Override
    public ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(Headers headers, ICall<GetBaseReportRequest> request) {
        return sendRequest(request, GetBaseReportRequest.class, GetBaseReportResponse.class);
    }

    @Override
    public ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(Headers headers, ICall<GetChargingProfilesRequest> request) {
        return sendRequest(request, GetChargingProfilesRequest.class, GetChargingProfilesResponse.class);
    }

    @Override
    public ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(Headers headers, ICall<GetCompositeScheduleRequest> request) {
        return sendRequest(request, GetCompositeScheduleRequest.class, GetCompositeScheduleResponse.class);
    }

    @Override
    public ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(Headers headers, ICall<GetDisplayMessagesRequest> request) {
        return sendRequest(request, GetDisplayMessagesRequest.class, GetDisplayMessagesResponse.class);
    }

    @Override
    public ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(Headers headers, ICall<GetInstalledCertificateIdsRequest> request) {
        return sendRequest(request, GetInstalledCertificateIdsRequest.class, GetInstalledCertificateIdsResponse.class);
    }

    @Override
    public ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(Headers headers, ICall<GetLocalListVersionRequest> request) {
        return sendRequest(request, GetLocalListVersionRequest.class, GetLocalListVersionResponse.class);
    }

    @Override
    public ICallResult<GetLogResponse> sendGetLogRequest(Headers headers, ICall<GetLogRequest> request) {
        return sendRequest(request, GetLogRequest.class, GetLogResponse.class);
    }

    @Override
    public ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(Headers headers, ICall<GetMonitoringReportRequest> request) {
        return sendRequest(request, GetMonitoringReportRequest.class, GetMonitoringReportResponse.class);
    }

    @Override
    public ICallResult<GetReportResponse> sendGetReportRequest(Headers headers, ICall<GetReportRequest> request) {
        return sendRequest(request, GetReportRequest.class, GetReportResponse.class);
    }

    @Override
    public ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(Headers headers, ICall<GetTransactionStatusRequest> request) {
        return sendRequest(request, GetTransactionStatusRequest.class, GetTransactionStatusResponse.class);
    }

    @Override
    public ICallResult<GetVariablesResponse> sendGetVariablesRequest(Headers headers, ICall<GetVariablesRequest> request) {
        return sendRequest(request, GetVariablesRequest.class, GetVariablesResponse.class);
    }

    @Override
    public ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(Headers headers, ICall<InstallCertificateRequest> request) {
        return sendRequest(request, InstallCertificateRequest.class, InstallCertificateResponse.class);
    }

    @Override
    public ICallResult<MeterValuesResponse> sendMeterValuesRequest(Headers headers, ICall<MeterValuesRequest> request) {
        return sendRequest(request, MeterValuesRequest.class, MeterValuesResponse.class);
    }

    @Override
    public ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(Headers headers, ICall<RequestStartTransactionRequest> request) {
        return sendRequest(request, RequestStartTransactionRequest.class, RequestStartTransactionResponse.class);
    }

    @Override
    public ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(Headers headers, ICall<RequestStopTransactionRequest> request) {
        return sendRequest(request, RequestStopTransactionRequest.class, RequestStopTransactionResponse.class);
    }

    @Override
    public ICallResult<ReserveNowResponse> sendReserveNowRequest(Headers headers, ICall<ReserveNowRequest> request) {
        return sendRequest(request, ReserveNowRequest.class, ReserveNowResponse.class);
    }

    @Override
    public ICallResult<ResetResponse> sendResetRequest(Headers headers, ICall<ResetRequest> request) {
        return sendRequest(request, ResetRequest.class, ResetResponse.class);
    }

    @Override
    public ICallResult<SendLocalListResponse> sendSendLocalListRequest(Headers headers, ICall<SendLocalListRequest> request) {
        return sendRequest(request, SendLocalListRequest.class, SendLocalListResponse.class);
    }

    @Override
    public ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(Headers headers, ICall<SetChargingProfileRequest> request) {
        return sendRequest(request, SetChargingProfileRequest.class, SetChargingProfileResponse.class);
    }

    @Override
    public ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(Headers headers, ICall<SetDisplayMessageRequest> request) {
        return sendRequest(request, SetDisplayMessageRequest.class, SetDisplayMessageResponse.class);
    }

    @Override
    public ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(Headers headers, ICall<SetMonitoringBaseRequest> request) {
        return sendRequest(request, SetMonitoringBaseRequest.class, SetMonitoringBaseResponse.class);
    }

    @Override
    public ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(Headers headers, ICall<SetMonitoringLevelRequest> request) {
        return sendRequest(request, SetMonitoringLevelRequest.class, SetMonitoringLevelResponse.class);
    }

    @Override
    public ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(Headers headers, ICall<SetNetworkProfileRequest> request) {
        return sendRequest(request, SetNetworkProfileRequest.class, SetNetworkProfileResponse.class);
    }

    @Override
    public ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(Headers headers, ICall<SetVariableMonitoringRequest> request) {
        return sendRequest(request, SetVariableMonitoringRequest.class, SetVariableMonitoringResponse.class);
    }

    @Override
    public ICallResult<SetVariablesResponse> sendSetVariablesRequest(Headers headers, ICall<SetVariablesRequest> request) {
        return sendRequest(request, SetVariablesRequest.class, SetVariablesResponse.class);
    }

    @Override
    public ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(Headers headers, ICall<TriggerMessageRequest> request) {
        return sendRequest(request, TriggerMessageRequest.class, TriggerMessageResponse.class);
    }

    @Override
    public ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(Headers headers, ICall<UnlockConnectorRequest> request) {
        return sendRequest(request, UnlockConnectorRequest.class, UnlockConnectorResponse.class);
    }

    @Override
    public ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(Headers headers, ICall<UnpublishFirmwareRequest> request) {
        return sendRequest(request, UnpublishFirmwareRequest.class, UnpublishFirmwareResponse.class);
    }

    @Override
    public ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(Headers headers, ICall<UpdateFirmwareRequest> request) {
        return sendRequest(request, UpdateFirmwareRequest.class, UpdateFirmwareResponse.class);
    }
}
