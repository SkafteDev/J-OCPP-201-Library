package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * This class is intended to represent a service endpoint from CS -> CSMS
 */
public class CsmsServiceEndpoint implements ICsmsServiceEndpoint {

    /**
     * TODO:Implement a statechart that tracks whether a given request can be serviced.
     * Only a single request can be serviced at any given moment.
     */

    private final ICsmsService.HandshakeRequest handshakeRequest;

    public CsmsServiceEndpoint(ICsmsService.HandshakeRequest handshakeRequest) {
        this.handshakeRequest = handshakeRequest;
    }

    public ICsmsService.HandshakeRequest getHandshakeInfo() {
        return handshakeRequest;
    }

    @Override
    public ICallResult<AuthorizeResponse> sendAuthorizeRequest(ICall<AuthorizeRequest> req) {
        return null;
    }

    @Override
    public ICallResult<BootNotificationResponse> sendBootNotificationRequest(ICall<BootNotificationRequest> req) {
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
    public ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(ICall<ClearedChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(ICall<FirmwareStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(ICall<Get15118EVCertificateRequest> req) {
        return null;
    }

    @Override
    public ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(ICall<GetCertificateStatusRequest> req) {
        return null;
    }

    @Override
    public ICallResult<HeartbeatResponse> sendHeartbeatRequest(ICall<HeartbeatRequest> req) {
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
    public ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(ICall<LogStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(ICall<NotifyChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(ICall<NotifyCustomerInformationRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(ICall<NotifyDisplayMessagesRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(ICall<NotifyEVChargingNeedsRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(ICall<NotifyEVChargingScheduleRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyEventResponse> sendNotifyEventRequest(ICall<NotifyEventRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(ICall<NotifyMonitoringReportRequest> req) {
        return null;
    }

    @Override
    public ICallResult<NotifyReportResponse> sendNotifyReportRequest(ICall<NotifyReportRequest> req) {
        return null;
    }

    @Override
    public ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(ICall<PublishFirmwareRequest> req) {
        return null;
    }

    @Override
    public ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(ICall<ReportChargingProfilesRequest> req) {
        return null;
    }

    @Override
    public ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(ICall<ReservationStatusUpdateRequest> req) {
        return null;
    }

    @Override
    public ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(ICall<SecurityEventNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResult<SignCertificateResponse> sendSignCertificateRequest(ICall<SignCertificateRequest> req) {
        return null;
    }

    @Override
    public ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(ICall<StatusNotificationRequest> req) {
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
    public ICallResult<TransactionEventResponse> sendTransactionEventRequest(ICall<TransactionEventRequest> req) {
        return null;
    }
}
