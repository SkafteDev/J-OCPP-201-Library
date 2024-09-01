package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Logger;

public class CsmsEndpoint implements ICsmsEndpoint {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String csmsId;
    private final ISessionManager sessionManager;

    public CsmsEndpoint(String csmsId, ISessionManager sessionManager) {
        this.csmsId = csmsId;
        this.sessionManager = sessionManager;
    }

    @Override
    public ICallResult<AuthorizeResponse> sendAuthorizeRequest(Headers headers, ICall<AuthorizeRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.AuthorizeRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<BootNotificationResponse> sendBootNotificationRequest(Headers headers, ICall<BootNotificationRequest> req) {
        boolean isCsIdPresent = headers.contains(Headers.HeaderEnum.CS_ID.getValue());

        return acceptBootNotificationRequest(req.getMessageId());
    }

    private ICallResult<BootNotificationResponse> acceptBootNotificationRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(now)
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResultMessage =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }

    private ICallResult<BootNotificationResponse> rejectBootNotificationRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.REJECTED)
                .withCurrentTime(now)
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResultMessage =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(Headers headers, ICall<ClearedChargingLimitRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearedChargingLimitRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(Headers headers, ICall<FirmwareStatusNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.FirmwareStatusNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(Headers headers, ICall<Get15118EVCertificateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.Get15118EVCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(Headers headers, ICall<GetCertificateStatusRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetCertificateStatusRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<HeartbeatResponse> sendHeartbeatRequest(Headers headers, ICall<HeartbeatRequest> req) {
        return acceptHeartbeatRequest(req.getMessageId());
    }

    private ICallResult<HeartbeatResponse> acceptHeartbeatRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        HeartbeatResponse response = HeartbeatResponse.builder()
                .withCurrentTime(now)
                .build();

        ICallResult<HeartbeatResponse> callResultMessage =
                CallResultImpl.<HeartbeatResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(response)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(Headers headers, ICall<LogStatusNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.LogStatusNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(Headers headers, ICall<NotifyChargingLimitRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyChargingLimitRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(Headers headers, ICall<NotifyCustomerInformationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyCustomerInformationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(Headers headers, ICall<NotifyDisplayMessagesRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyDisplayMessagesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(Headers headers, ICall<NotifyEVChargingNeedsRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEVChargingNeedsRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(Headers headers, ICall<NotifyEVChargingScheduleRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEVChargingScheduleRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEventResponse> sendNotifyEventRequest(Headers headers, ICall<NotifyEventRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEventRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(Headers headers, ICall<NotifyMonitoringReportRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyMonitoringReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyReportResponse> sendNotifyReportRequest(Headers headers, ICall<NotifyReportRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(Headers headers, ICall<PublishFirmwareRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.PublishFirmwareRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(Headers headers, ICall<ReportChargingProfilesRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ReportChargingProfilesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(Headers headers, ICall<ReservationStatusUpdateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ReservationStatusUpdateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(Headers headers, ICall<SecurityEventNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SecurityEventNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SignCertificateResponse> sendSignCertificateRequest(Headers headers, ICall<SignCertificateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SignCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(Headers headers, ICall<StatusNotificationRequest> req) {
        return acceptStatusNotificationRequest(req.getMessageId());
    }

    private ICallResult<StatusNotificationResponse> acceptStatusNotificationRequest(String responseMsgId) {
        StatusNotificationResponse statusNotificationResponse = StatusNotificationResponse.builder()
                // No fields required as specified in OCPP 2.0.1.
                .build();

        ICallResult<StatusNotificationResponse> callResultMessage =
                CallResultImpl.<StatusNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(statusNotificationResponse)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<TransactionEventResponse> sendTransactionEventRequest(Headers headers, ICall<TransactionEventRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.TransactionEventRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }
}
