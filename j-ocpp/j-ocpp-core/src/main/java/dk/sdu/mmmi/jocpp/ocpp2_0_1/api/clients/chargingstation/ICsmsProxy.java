package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;

/**
 * This interface defines the Charging Station Client API.
 *
 * The operations in this interface represents the requests that a Charging Station can invoke on a Charging Station Management System.
 *
 * The interface consists of sub-interfaces, each mapping to a functional block in the OCPP 2.0.1 specification.
 *
 * For example:
 * Provisioning API
 * SmartCharging API
 * Authentication API
 * etc.
 */
public interface ICsmsProxy {
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

    /**
     * This operation is NOT part of OCPP 2.0.1.
     *
     * This operation returns a message routing map that can be used to look up the route for a specific message type.
     * @return
     */
    IMessageRouteResolver getRouteResolver();
}
