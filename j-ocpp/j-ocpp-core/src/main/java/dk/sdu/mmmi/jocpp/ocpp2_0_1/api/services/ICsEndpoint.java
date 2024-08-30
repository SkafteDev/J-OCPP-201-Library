package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

public interface ICsEndpoint {
    ICallResult<CancelReservationResponse> sendCancelReservationRequest(Headers headers, ICall<CancelReservationRequest> request);
    ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(Headers headers, ICall<CertificateSignedRequest> request);
    ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(Headers headers, ICall<ChangeAvailabilityRequest> request);
    ICallResult<ClearCacheResponse> sendClearCacheRequest(Headers headers, ICall<ClearCacheRequest> request);
    ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(Headers headers, ICall<ClearChargingProfileRequest> request);
    ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(Headers headers, ICall<ClearDisplayMessageRequest> request);
    ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(Headers headers, ICall<ClearVariableMonitoringRequest> request);
    ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(Headers headers, ICall<CostUpdatedRequest> request);
    ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(Headers headers, ICall<CustomerInformationRequest> request);
    ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(Headers headers, ICall<DeleteCertificateRequest> request);
    ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(Headers headers, ICall<GetBaseReportRequest> request);
    ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(Headers headers, ICall<GetChargingProfilesRequest> request);
    ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(Headers headers, ICall<GetCompositeScheduleRequest> request);
    ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(Headers headers, ICall<GetDisplayMessagesRequest> request);
    ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(Headers headers, ICall<GetInstalledCertificateIdsRequest> request);
    ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(Headers headers, ICall<GetLocalListVersionRequest> request);
    ICallResult<GetLogResponse> sendGetLogRequest(Headers headers, ICall<GetLogRequest> request);
    ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(Headers headers, ICall<GetMonitoringReportRequest> request);
    ICallResult<GetReportResponse> sendGetReportRequest(Headers headers, ICall<GetReportRequest> request);
    ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(Headers headers, ICall<GetTransactionStatusRequest> request);
    ICallResult<GetVariablesResponse> sendGetVariablesRequest(Headers headers, ICall<GetVariablesRequest> request);
    ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(Headers headers, ICall<InstallCertificateRequest> request);
    ICallResult<MeterValuesResponse> sendMeterValuesRequest(Headers headers, ICall<MeterValuesRequest> request);
    ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(Headers headers, ICall<RequestStartTransactionRequest> request);
    ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(Headers headers, ICall<RequestStopTransactionRequest> request);
    ICallResult<ReserveNowResponse> sendReserveNowRequest(Headers headers, ICall<ReserveNowRequest> request);
    ICallResult<ResetResponse> sendResetRequest(Headers headers, ICall<ResetRequest> request);
    ICallResult<SendLocalListResponse> sendSendLocalListRequest(Headers headers, ICall<SendLocalListRequest> request);
    ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(Headers headers, ICall<SetChargingProfileRequest> request);
    ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(Headers headers, ICall<SetDisplayMessageRequest> request);
    ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(Headers headers, ICall<SetMonitoringBaseRequest> request);
    ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(Headers headers, ICall<SetMonitoringLevelRequest> request);
    ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(Headers headers, ICall<SetNetworkProfileRequest> request);
    ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(Headers headers, ICall<SetVariableMonitoringRequest> request);
    ICallResult<SetVariablesResponse> sendSetVariablesRequest(Headers headers, ICall<SetVariablesRequest> request);
    ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(Headers headers, ICall<TriggerMessageRequest> request);
    ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(Headers headers, ICall<UnlockConnectorRequest> request);
    ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(Headers headers, ICall<UnpublishFirmwareRequest> request);
    ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(Headers headers, ICall<UpdateFirmwareRequest> request);
}
