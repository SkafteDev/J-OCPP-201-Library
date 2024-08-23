package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.util.logging.Logger;

/**
 * This class holds the implementation of a ChargingStation including
 * all the requests that it should be able to handle.
 */
public class CSServiceEndpointImpl implements ICsServiceEndpoint {
    private final Logger logger = Logger.getLogger(CSServiceEndpointImpl.class.getName());

    @Override
    public ICallResult<CancelReservationResponse> sendCancelReservationRequest(ICall<CancelReservationRequest> request) {
        return null;
    }

    @Override
    public ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(ICall<CertificateSignedRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(ICall<ChangeAvailabilityRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ClearCacheResponse> sendClearCacheRequest(ICall<ClearCacheRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(ICall<ClearChargingProfileRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(ICall<ClearDisplayMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(ICall<ClearVariableMonitoringRequest> request) {
        return null;
    }

    @Override
    public ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(ICall<CostUpdatedRequest> request) {
        return null;
    }

    @Override
    public ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(ICall<CustomerInformationRequest> request) {
        return null;
    }

    @Override
    public ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(ICall<DeleteCertificateRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(ICall<GetBaseReportRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(ICall<GetChargingProfilesRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(ICall<GetCompositeScheduleRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(ICall<GetDisplayMessagesRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(ICall<GetInstalledCertificateIdsRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(ICall<GetLocalListVersionRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetLogResponse> sendGetLogRequest(ICall<GetLogRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(ICall<GetMonitoringReportRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetReportResponse> sendGetReportRequest(ICall<GetReportRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(ICall<GetTransactionStatusRequest> request) {
        return null;
    }

    @Override
    public ICallResult<GetVariablesResponse> sendGetVariablesRequest(ICall<GetVariablesRequest> request) {
        return null;
    }

    @Override
    public ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(ICall<InstallCertificateRequest> request) {
        return null;
    }

    @Override
    public ICallResult<MeterValuesResponse> sendMeterValuesRequest(ICall<MeterValuesRequest> request) {
        return null;
    }

    @Override
    public ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(ICall<RequestStartTransactionRequest> request) {
        return null;
    }

    @Override
    public ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(ICall<RequestStopTransactionRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ReserveNowResponse> sendReserveNowRequest(ICall<ReserveNowRequest> request) {
        return null;
    }

    @Override
    public ICallResult<ResetResponse> sendResetRequest(ICall<ResetRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SendLocalListResponse> sendSendLocalListRequest(ICall<SendLocalListRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(ICall<SetChargingProfileRequest> request) {
        // TODO: Update the internal state of the CS
        SetChargingProfileRequest requestPayload = request.getPayload();
        logger.info("SetChargingProfileRequest received.");

        // TODO: Create response depending on the internal state...
        SetChargingProfileResponse responsePayload = SetChargingProfileResponse.builder()
                .withStatus(ChargingProfileStatusEnum.ACCEPTED)
                .build();

        ICallResult<SetChargingProfileResponse> callResult =
                CallResultImpl.<SetChargingProfileResponse>newBuilder()
                        .withMessageId(request.getMessageId()) // MessageId MUST be identical to the call.
                        .withPayLoad(responsePayload)
                        .build();

        return callResult;
    }

    @Override
    public ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(ICall<SetDisplayMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(ICall<SetMonitoringBaseRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(ICall<SetMonitoringLevelRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICall<SetNetworkProfileRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(ICall<SetVariableMonitoringRequest> request) {
        return null;
    }

    @Override
    public ICallResult<SetVariablesResponse> sendSetVariablesRequest(ICall<SetVariablesRequest> request) {
        return null;
    }

    @Override
    public ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(ICall<TriggerMessageRequest> request) {
        return null;
    }

    @Override
    public ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(ICall<UnlockConnectorRequest> request) {
        return null;
    }

    @Override
    public ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(ICall<UnpublishFirmwareRequest> request) {
        return null;
    }

    @Override
    public ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(ICall<UpdateFirmwareRequest> request) {
        return null;
    }
}
