package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;

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
public interface IChargingStationClientApi {
    ICallResultMessage<AuthorizeResponse> sendAuthorizeRequest(ICallMessage<AuthorizeRequest> req);
    ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req);
    ICallResultMessage<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(ICallMessage<ClearedChargingLimitRequest> req);
    ICallResultMessage<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(ICallMessage<FirmwareStatusNotificationRequest> req);
    ICallResultMessage<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(ICallMessage<Get15118EVCertificateRequest> req);
    ICallResultMessage<GetCertificateStatusResponse> sendGetCertificateStatusRequest(ICallMessage<GetCertificateStatusRequest> req);
    ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req);
    ICallResultMessage<LogStatusNotificationResponse> sendLogStatusNotificationRequest(ICallMessage<LogStatusNotificationRequest> req);
    ICallResultMessage<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(ICallMessage<NotifyChargingLimitRequest> req);
    ICallResultMessage<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(ICallMessage<NotifyCustomerInformationRequest> req);
    ICallResultMessage<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(ICallMessage<NotifyDisplayMessagesRequest> req);
    ICallResultMessage<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(ICallMessage<NotifyEVChargingNeedsRequest> req);
    ICallResultMessage<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(ICallMessage<NotifyEVChargingScheduleRequest> req);
    ICallResultMessage<NotifyEventResponse> sendNotifyEventRequest(ICallMessage<NotifyEventRequest> req);
    ICallResultMessage<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(ICallMessage<NotifyMonitoringReportRequest> req);
    ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req);
    ICallResultMessage<PublishFirmwareResponse> sendPublishFirmwareRequest(ICallMessage<PublishFirmwareRequest> req);
    ICallResultMessage<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(ICallMessage<ReportChargingProfilesRequest> req);
    ICallResultMessage<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(ICallMessage<ReservationStatusUpdateRequest> req);
    ICallResultMessage<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(ICallMessage<SecurityEventNotificationRequest> req);
    ICallResultMessage<SignCertificateResponse> sendSignCertificateRequest(ICallMessage<SignCertificateRequest> req);
    ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req);
    ICallResultMessage<TransactionEventResponse> sendTransactionEventRequest(ICallMessage<TransactionEventRequest> req);

    /**
     * This operation is NOT part of OCPP 2.0.1.
     *
     * This operation returns a message routing map that can be used to look up the route for a specific message type.
     * @return
     */
    IMessageRoutingMap getMessageRoutingMap();
}
