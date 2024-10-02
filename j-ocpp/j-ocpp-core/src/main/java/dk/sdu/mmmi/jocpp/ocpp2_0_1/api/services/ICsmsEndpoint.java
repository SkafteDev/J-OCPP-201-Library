/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

public interface ICsmsEndpoint {
    ICallResult<AuthorizeResponse> sendAuthorizeRequest(Headers headers, ICall<AuthorizeRequest> req);
    ICallResult<BootNotificationResponse> sendBootNotificationRequest(Headers headers,  ICall<BootNotificationRequest> req);
    ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(Headers headers,  ICall<ClearedChargingLimitRequest> req);
    ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(Headers headers,  ICall<FirmwareStatusNotificationRequest> req);
    ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(Headers headers,  ICall<Get15118EVCertificateRequest> req);
    ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(Headers headers,  ICall<GetCertificateStatusRequest> req);
    ICallResult<HeartbeatResponse> sendHeartbeatRequest(Headers headers,  ICall<HeartbeatRequest> req);
    ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(Headers headers,  ICall<LogStatusNotificationRequest> req);
    ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(Headers headers,  ICall<NotifyChargingLimitRequest> req);
    ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(Headers headers,  ICall<NotifyCustomerInformationRequest> req);
    ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(Headers headers,  ICall<NotifyDisplayMessagesRequest> req);
    ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(Headers headers,  ICall<NotifyEVChargingNeedsRequest> req);
    ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(Headers headers,  ICall<NotifyEVChargingScheduleRequest> req);
    ICallResult<NotifyEventResponse> sendNotifyEventRequest(Headers headers,  ICall<NotifyEventRequest> req);
    ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(Headers headers,  ICall<NotifyMonitoringReportRequest> req);
    ICallResult<NotifyReportResponse> sendNotifyReportRequest(Headers headers,  ICall<NotifyReportRequest> req);
    ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(Headers headers,  ICall<PublishFirmwareRequest> req);
    ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(Headers headers,  ICall<ReportChargingProfilesRequest> req);
    ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(Headers headers,  ICall<ReservationStatusUpdateRequest> req);
    ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(Headers headers,  ICall<SecurityEventNotificationRequest> req);
    ICallResult<SignCertificateResponse> sendSignCertificateRequest(Headers headers,  ICall<SignCertificateRequest> req);
    ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(Headers headers,  ICall<StatusNotificationRequest> req);
    ICallResult<TransactionEventResponse> sendTransactionEventRequest(Headers headers,  ICall<TransactionEventRequest> req);
}
