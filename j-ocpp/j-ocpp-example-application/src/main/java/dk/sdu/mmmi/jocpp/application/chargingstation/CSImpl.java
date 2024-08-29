package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IChargingStation;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.util.logging.Logger;

/**
 * This class holds the implementation of a ChargingStation including
 * all the requests that it should be able to handle.
 */
public class CSImpl implements IChargingStation {
    private final Logger logger = Logger.getLogger(CSImpl.class.getName());

    @Override
    public ICallResult<CancelReservationResponse> sendCancelReservationRequest(Headers headers, ICall<CancelReservationRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.CancelReservationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<CertificateSignedResponse> sendCertificateSignedRequest(Headers headers,ICall<CertificateSignedRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.CertificateSignedRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ChangeAvailabilityResponse> sendChangeAvailabilityRequest(Headers headers,ICall<ChangeAvailabilityRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ChangeAvailabilityRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ClearCacheResponse> sendClearCacheRequest(Headers headers,ICall<ClearCacheRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearCacheRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ClearChargingProfileResponse> sendClearChargingProfileRequest(Headers headers,ICall<ClearChargingProfileRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearChargingProfileRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ClearDisplayMessageResponse> sendClearDisplayMessageRequest(Headers headers,ICall<ClearDisplayMessageRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearDisplayMessageRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ClearVariableMonitoringResponse> sendClearVariableMonitoringRequest(Headers headers,ICall<ClearVariableMonitoringRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearVariableMonitoringRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<CostUpdatedResponse> sendCostUpdatedRequest(Headers headers,ICall<CostUpdatedRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.CostUpdatedRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<CustomerInformationResponse> sendCustomerInformationRequest(Headers headers,ICall<CustomerInformationRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.CustomerInformationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<DeleteCertificateResponse> sendDeleteCertificateRequest(Headers headers,ICall<DeleteCertificateRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.DeleteCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetBaseReportResponse> sendGetBaseReportRequest(Headers headers,ICall<GetBaseReportRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetBaseReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetChargingProfilesResponse> sendGetChargingProfilesRequest(Headers headers,ICall<GetChargingProfilesRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetChargingProfilesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetCompositeScheduleResponse> sendGetCompositeScheduleRequest(Headers headers,ICall<GetCompositeScheduleRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetCompositeScheduleRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetDisplayMessagesResponse> sendGetDisplayMessagesRequest(Headers headers,ICall<GetDisplayMessagesRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetDisplayMessagesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetInstalledCertificateIdsResponse> sendGetInstalledCertificateIdsRequest(Headers headers,ICall<GetInstalledCertificateIdsRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetInstalledCertificateIdsRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetLocalListVersionResponse> sendGetLocalListVersionRequest(Headers headers,ICall<GetLocalListVersionRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetLocalListVersionRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetLogResponse> sendGetLogRequest(Headers headers,ICall<GetLogRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetLogRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetMonitoringReportResponse> sendGetMonitoringReportRequest(Headers headers,ICall<GetMonitoringReportRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetMonitoringReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetReportResponse> sendGetReportRequest(Headers headers,ICall<GetReportRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetTransactionStatusResponse> sendGetTransactionStatusRequest(Headers headers,ICall<GetTransactionStatusRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetTransactionStatusRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetVariablesResponse> sendGetVariablesRequest(Headers headers,ICall<GetVariablesRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetVariablesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<InstallCertificateResponse> sendInstallCertificateRequest(Headers headers,ICall<InstallCertificateRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.InstallCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<MeterValuesResponse> sendMeterValuesRequest(Headers headers,ICall<MeterValuesRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.MeterValuesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<RequestStartTransactionResponse> sendRequestStartTransactionRequest(Headers headers,ICall<RequestStartTransactionRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.RequestStartTransactionRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<RequestStopTransactionResponse> sendRequestStopTransactionRequest(Headers headers,ICall<RequestStopTransactionRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.RequestStopTransactionRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ReserveNowResponse> sendReserveNowRequest(Headers headers,ICall<ReserveNowRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ReserveNowRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ResetResponse> sendResetRequest(Headers headers,ICall<ResetRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ResetRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SendLocalListResponse> sendSendLocalListRequest(Headers headers,ICall<SendLocalListRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SendLocalListRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetChargingProfileResponse> sendSetChargingProfileRequest(Headers headers,ICall<SetChargingProfileRequest> request) {
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
    public ICallResult<SetDisplayMessageResponse> sendSetDisplayMessageRequest(Headers headers,ICall<SetDisplayMessageRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetDisplayMessageResponse))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetMonitoringBaseResponse> sendSetMonitoringBaseRequest(Headers headers,ICall<SetMonitoringBaseRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetMonitoringBaseRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetMonitoringLevelResponse> sendSetMonitoringLevelRequest(Headers headers,ICall<SetMonitoringLevelRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetMonitoringLevelRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetNetworkProfileResponse> sendSetNetworkProfileRequest(Headers headers,ICall<SetNetworkProfileRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetNetworkProfileRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetVariableMonitoringResponse> sendSetVariableMonitoringRequest(Headers headers,ICall<SetVariableMonitoringRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetVariableMonitoringRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SetVariablesResponse> sendSetVariablesRequest(Headers headers,ICall<SetVariablesRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SetVariablesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<TriggerMessageResponse> sendTriggerMessageRequest(Headers headers,ICall<TriggerMessageRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.TriggerMessageRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<UnlockConnectorResponse> sendUnlockConnectorRequest(Headers headers,ICall<UnlockConnectorRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.UnlockConnectorRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<UnpublishFirmwareResponse> sendUnpublishFirmwareRequest(Headers headers,ICall<UnpublishFirmwareRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.UnpublishFirmwareRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<UpdateFirmwareResponse> sendUpdateFirmwareRequest(Headers headers,ICall<UpdateFirmwareRequest> request) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(request.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.UpdateFirmwareRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }
}
