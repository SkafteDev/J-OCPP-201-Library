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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.proxies;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.OCPPOverNatsIORequester;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class CsmsEndpointOverNatsIoProxy implements ICsmsEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(CsmsEndpointOverNatsIoProxy.class.getName());

    private final Connection natsConnection;

    private final IMessageRouteResolver routeResolver;

    public CsmsEndpointOverNatsIoProxy(Connection natsConnection, IMessageRouteResolver routingResolver) {
        this.natsConnection = natsConnection;
        this.routeResolver = routingResolver;
    }

    private <TRequest, TResponse> ICallResult<TResponse> sendRequest(ICall<TRequest> req, Class<TRequest> requestClass, Class<TResponse> responseClass) {
        var requester = new OCPPOverNatsIORequester<>(requestClass, responseClass);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.valueOf(requestClass.getSimpleName()));
        String responseSubject = routeResolver.getRoute(OCPPMessageType.valueOf(responseClass.getSimpleName()));

        try {
            ICallResult<TResponse> response = requester.request(req, requestSubject, responseSubject, natsConnection);
            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ICallResult<AuthorizeResponse> sendAuthorizeRequest(Headers headers, ICall<AuthorizeRequest> req) {
        return sendRequest(req, AuthorizeRequest.class, AuthorizeResponse.class);
    }

    @Override
    public ICallResult<BootNotificationResponse> sendBootNotificationRequest(Headers headers, ICall<BootNotificationRequest> req) {
        return sendRequest(req, BootNotificationRequest.class, BootNotificationResponse.class);
    }

    @Override
    public ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(Headers headers, ICall<ClearedChargingLimitRequest> req) {
        return sendRequest(req, ClearedChargingLimitRequest.class, ClearedChargingLimitResponse.class);
    }

    @Override
    public ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(Headers headers, ICall<FirmwareStatusNotificationRequest> req) {
        return sendRequest(req, FirmwareStatusNotificationRequest.class, FirmwareStatusNotificationResponse.class);
    }

    @Override
    public ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(Headers headers, ICall<Get15118EVCertificateRequest> req) {
        return sendRequest(req, Get15118EVCertificateRequest.class, Get15118EVCertificateResponse.class);
    }

    @Override
    public ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(Headers headers, ICall<GetCertificateStatusRequest> req) {
        return sendRequest(req, GetCertificateStatusRequest.class, GetCertificateStatusResponse.class);
    }

    @Override
    public ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(Headers headers, ICall<StatusNotificationRequest> req) {
        return sendRequest(req, StatusNotificationRequest.class, StatusNotificationResponse.class);
    }

    @Override
    public ICallResult<TransactionEventResponse> sendTransactionEventRequest(Headers headers, ICall<TransactionEventRequest> req) {
        return sendRequest(req, TransactionEventRequest.class, TransactionEventResponse.class);
    }

    @Override
    public ICallResult<HeartbeatResponse> sendHeartbeatRequest(Headers headers, ICall<HeartbeatRequest> req) {
        return sendRequest(req, HeartbeatRequest.class, HeartbeatResponse.class);
    }

    @Override
    public ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(Headers headers, ICall<LogStatusNotificationRequest> req) {
        return sendRequest(req, LogStatusNotificationRequest.class, LogStatusNotificationResponse.class);
    }

    @Override
    public ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(Headers headers, ICall<NotifyChargingLimitRequest> req) {
        return sendRequest(req, NotifyChargingLimitRequest.class, NotifyChargingLimitResponse.class);
    }

    @Override
    public ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(Headers headers, ICall<NotifyCustomerInformationRequest> req) {
        return sendRequest(req, NotifyCustomerInformationRequest.class, NotifyCustomerInformationResponse.class);
    }

    @Override
    public ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(Headers headers, ICall<NotifyDisplayMessagesRequest> req) {
        return sendRequest(req, NotifyDisplayMessagesRequest.class, NotifyDisplayMessagesResponse.class);
    }

    @Override
    public ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(Headers headers, ICall<NotifyEVChargingNeedsRequest> req) {
        return sendRequest(req, NotifyEVChargingNeedsRequest.class, NotifyEVChargingNeedsResponse.class);
    }

    @Override
    public ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(Headers headers, ICall<NotifyEVChargingScheduleRequest> req) {
        return sendRequest(req, NotifyEVChargingScheduleRequest.class, NotifyEVChargingScheduleResponse.class);
    }

    @Override
    public ICallResult<NotifyEventResponse> sendNotifyEventRequest(Headers headers, ICall<NotifyEventRequest> req) {
        return sendRequest(req, NotifyEventRequest.class, NotifyEventResponse.class);
    }

    @Override
    public ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(Headers headers, ICall<NotifyMonitoringReportRequest> req) {
        return sendRequest(req, NotifyMonitoringReportRequest.class, NotifyMonitoringReportResponse.class);
    }

    @Override
    public ICallResult<NotifyReportResponse> sendNotifyReportRequest(Headers headers, ICall<NotifyReportRequest> req) {
        return sendRequest(req, NotifyReportRequest.class, NotifyReportResponse.class);
    }

    @Override
    public ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(Headers headers, ICall<PublishFirmwareRequest> req) {
        return sendRequest(req, PublishFirmwareRequest.class, PublishFirmwareResponse.class);
    }

    @Override
    public ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(Headers headers, ICall<ReportChargingProfilesRequest> req) {
        return sendRequest(req, ReportChargingProfilesRequest.class, ReportChargingProfilesResponse.class);
    }

    @Override
    public ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(Headers headers, ICall<ReservationStatusUpdateRequest> req) {
        return sendRequest(req, ReservationStatusUpdateRequest.class, ReservationStatusUpdateResponse.class);
    }

    @Override
    public ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(Headers headers, ICall<SecurityEventNotificationRequest> req) {
        return sendRequest(req, SecurityEventNotificationRequest.class, SecurityEventNotificationResponse.class);
    }

    @Override
    public ICallResult<SignCertificateResponse> sendSignCertificateRequest(Headers headers, ICall<SignCertificateRequest> req) {
        return sendRequest(req, SignCertificateRequest.class, SignCertificateResponse.class);
    }
}
