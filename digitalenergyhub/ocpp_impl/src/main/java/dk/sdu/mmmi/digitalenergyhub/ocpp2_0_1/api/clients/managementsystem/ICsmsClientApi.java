package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;

/**
 * This interface defines the Charging Station Management System Client API.
 *
 * The operations in this interface represents the requests that a Charging Station Management System can invoke on
 * a Charging Station.
 *
 */
public interface ICsmsClientApi {
    ICallResultMessage<CancelReservationResponse> sendCancelReservationRequest(ICallMessage<CancelReservationRequest> request);
    ICallResultMessage<CertificateSignedResponse> sendCertificateSignedRequest(ICallMessage<CertificateSignedRequest> request);
    ICallResultMessage<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(ICallMessage<ChangeAvailabilityRequest> request);
    ICallResultMessage<ClearCacheResponse> sendClearCacheRequest(ICallMessage<ClearCacheRequest> request);
    ICallResultMessage<ClearChargingProfileResponse> sendClearChargingProfileRequest(ICallMessage<ClearChargingProfileRequest> request);
    ICallResultMessage<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(ICallMessage<ClearDisplayMessageRequest> request);
    ICallResultMessage<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(ICallMessage<ClearVariableMonitoringRequest> request);
    ICallResultMessage<CostUpdatedResponse> sendCostUpdatedRequest(ICallMessage<CostUpdatedRequest> request);
    ICallResultMessage<CustomerInformationResponse> sendCustomerInformationRequest(ICallMessage<CustomerInformationRequest> request);
    ICallResultMessage<DeleteCertificateResponse> sendDeleteCertificateRequest(ICallMessage<DeleteCertificateRequest> request);
    ICallResultMessage<GetBaseReportResponse> sendGetBaseReportRequest(ICallMessage<GetBaseReportRequest> request);
    ICallResultMessage<GetChargingProfilesResponse> sendGetChargingProfilesRequest(ICallMessage<GetChargingProfilesRequest> request);
    ICallResultMessage<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(ICallMessage<GetCompositeScheduleRequest> request);
    ICallResultMessage<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(ICallMessage<GetDisplayMessagesRequest> request);
    ICallResultMessage<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(ICallMessage<GetInstalledCertificateIdsRequest> request);
    ICallResultMessage<GetLocalListVersionResponse> sendGetLocalListVersionRequest(ICallMessage<GetLocalListVersionRequest> request);
    ICallResultMessage<GetLogResponse> sendGetLogRequest(ICallMessage<GetLogRequest> request);
    ICallResultMessage<GetMonitoringReportResponse> sendGetMonitoringReportRequest(ICallMessage<GetMonitoringReportRequest> request);
    ICallResultMessage<GetReportResponse> sendGetReportRequest(ICallMessage<GetReportRequest> request);
    ICallResultMessage<GetTransactionStatusResponse> sendGetTransactionStatusRequest(ICallMessage<GetTransactionStatusRequest> request);
    ICallResultMessage<GetVariablesResponse> sendGetVariablesRequest(ICallMessage<GetVariablesRequest> request);
    ICallResultMessage<InstallCertificateResponse> sendInstallCertificateRequest(ICallMessage<InstallCertificateRequest> request);
    ICallResultMessage<MeterValuesResponse> sendMeterValuesRequest(ICallMessage<MeterValuesRequest> request);
    ICallResultMessage<RequestStartTransactionResponse> sendRequestStartTransactionRequest(ICallMessage<RequestStartTransactionRequest> request);
    ICallResultMessage<RequestStopTransactionResponse> sendRequestStopTransactionRequest(ICallMessage<RequestStopTransactionRequest> request);
    ICallResultMessage<ReserveNowResponse> sendReserveNowRequest(ICallMessage<ReserveNowRequest> request);
    ICallResultMessage<ResetResponse> sendResetRequest(ICallMessage<ResetRequest> request);
    ICallResultMessage<SendLocalListResponse> sendSendLocalListRequest(ICallMessage<SendLocalListRequest> request);
    ICallResultMessage<SetChargingProfileResponse> sendSetChargingProfileRequest(ICallMessage<SetChargingProfileRequest> request);
    ICallResultMessage<SetDisplayMessageResponse> sendSetDisplayMessageRequest(ICallMessage<SetDisplayMessageRequest> request);
    ICallResultMessage<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(ICallMessage<SetMonitoringBaseRequest> request);
    ICallResultMessage<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(ICallMessage<SetMonitoringLevelRequest> request);
    ICallResultMessage<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICallMessage<SetNetworkProfileRequest> request);
    ICallResultMessage<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(ICallMessage<SetVariableMonitoringRequest> request);
    ICallResultMessage<SetVariablesResponse> sendSetVariablesRequest(ICallMessage<SetVariablesRequest> request);
    ICallResultMessage<TriggerMessageResponse> sendTriggerMessageRequest(ICallMessage<TriggerMessageRequest> request);
    ICallResultMessage<UnlockConnectorResponse> sendUnlockConnectorRequest(ICallMessage<UnlockConnectorRequest> request);
    ICallResultMessage<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(ICallMessage<UnpublishFirmwareRequest> request);
    ICallResultMessage<UpdateFirmwareResponse> sendUpdateFirmwareRequest(ICallMessage<UpdateFirmwareRequest> request);
}
