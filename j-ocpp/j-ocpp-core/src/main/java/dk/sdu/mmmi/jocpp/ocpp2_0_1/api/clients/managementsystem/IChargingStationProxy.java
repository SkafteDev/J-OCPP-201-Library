package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.managementsystem;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;

/**
 * This interface defines the Charging Station Management System Client API.
 *
 * The operations in this interface represents the requests that a Charging Station Management System can invoke on
 * a Charging Station.
 *
 */
public interface IChargingStationProxy {
    ICallResult<CancelReservationResponse> sendCancelReservationRequest(ICall<CancelReservationRequest> request);
    ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(ICall<CertificateSignedRequest> request);
    ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(ICall<ChangeAvailabilityRequest> request);
    ICallResult<ClearCacheResponse> sendClearCacheRequest(ICall<ClearCacheRequest> request);
    ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(ICall<ClearChargingProfileRequest> request);
    ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(ICall<ClearDisplayMessageRequest> request);
    ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(ICall<ClearVariableMonitoringRequest> request);
    ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(ICall<CostUpdatedRequest> request);
    ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(ICall<CustomerInformationRequest> request);
    ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(ICall<DeleteCertificateRequest> request);
    ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(ICall<GetBaseReportRequest> request);
    ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(ICall<GetChargingProfilesRequest> request);
    ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(ICall<GetCompositeScheduleRequest> request);
    ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(ICall<GetDisplayMessagesRequest> request);
    ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(ICall<GetInstalledCertificateIdsRequest> request);
    ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(ICall<GetLocalListVersionRequest> request);
    ICallResult<GetLogResponse> sendGetLogRequest(ICall<GetLogRequest> request);
    ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(ICall<GetMonitoringReportRequest> request);
    ICallResult<GetReportResponse> sendGetReportRequest(ICall<GetReportRequest> request);
    ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(ICall<GetTransactionStatusRequest> request);
    ICallResult<GetVariablesResponse> sendGetVariablesRequest(ICall<GetVariablesRequest> request);
    ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(ICall<InstallCertificateRequest> request);
    ICallResult<MeterValuesResponse> sendMeterValuesRequest(ICall<MeterValuesRequest> request);
    ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(ICall<RequestStartTransactionRequest> request);
    ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(ICall<RequestStopTransactionRequest> request);
    ICallResult<ReserveNowResponse> sendReserveNowRequest(ICall<ReserveNowRequest> request);
    ICallResult<ResetResponse> sendResetRequest(ICall<ResetRequest> request);
    ICallResult<SendLocalListResponse> sendSendLocalListRequest(ICall<SendLocalListRequest> request);
    ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(ICall<SetChargingProfileRequest> request);
    ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(ICall<SetDisplayMessageRequest> request);
    ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(ICall<SetMonitoringBaseRequest> request);
    ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(ICall<SetMonitoringLevelRequest> request);
    ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICall<SetNetworkProfileRequest> request);
    ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(ICall<SetVariableMonitoringRequest> request);
    ICallResult<SetVariablesResponse> sendSetVariablesRequest(ICall<SetVariablesRequest> request);
    ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(ICall<TriggerMessageRequest> request);
    ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(ICall<UnlockConnectorRequest> request);
    ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(ICall<UnpublishFirmwareRequest> request);
    ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(ICall<UpdateFirmwareRequest> request);
}
