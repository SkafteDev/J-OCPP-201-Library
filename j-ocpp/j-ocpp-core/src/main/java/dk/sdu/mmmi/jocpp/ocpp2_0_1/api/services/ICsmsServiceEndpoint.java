package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;

/**
 * This Java interface defines a CSMS Service interface.
 * The operations in this interface represents the requests that a Charging Station can invoke on a Charging Station Management System.
 */
public interface ICsmsServiceEndpoint {
    ICallResult<AuthorizeResponse> sendAuthorizeRequest(ICall<AuthorizeRequest> req);
    ICallResult<BootNotificationResponse> sendBootNotificationRequest(ICall<BootNotificationRequest> req);
    ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(ICall<ClearedChargingLimitRequest> req);
    ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(ICall<FirmwareStatusNotificationRequest> req);
    ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(ICall<Get15118EVCertificateRequest> req);
    ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(ICall<GetCertificateStatusRequest> req);
    ICallResult<HeartbeatResponse> sendHeartbeatRequest(ICall<HeartbeatRequest> req);
    ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(ICall<LogStatusNotificationRequest> req);
    ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(ICall<NotifyChargingLimitRequest> req);
    ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(ICall<NotifyCustomerInformationRequest> req);
    ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(ICall<NotifyDisplayMessagesRequest> req);
    ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(ICall<NotifyEVChargingNeedsRequest> req);
    ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(ICall<NotifyEVChargingScheduleRequest> req);
    ICallResult<NotifyEventResponse> sendNotifyEventRequest(ICall<NotifyEventRequest> req);
    ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(ICall<NotifyMonitoringReportRequest> req);
    ICallResult<NotifyReportResponse> sendNotifyReportRequest(ICall<NotifyReportRequest> req);
    ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(ICall<PublishFirmwareRequest> req);
    ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(ICall<ReportChargingProfilesRequest> req);
    ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(ICall<ReservationStatusUpdateRequest> req);
    ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(ICall<SecurityEventNotificationRequest> req);
    ICallResult<SignCertificateResponse> sendSignCertificateRequest(ICall<SignCertificateRequest> req);
    ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(ICall<StatusNotificationRequest> req);
    ICallResult<TransactionEventResponse> sendTransactionEventRequest(ICall<TransactionEventRequest> req);
}
