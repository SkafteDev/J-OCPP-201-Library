package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.ICsmsProxy;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.OCPPRequester;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;

import java.util.logging.Logger;

public class CsmsProxyImpl implements ICsmsProxy {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Connection natsConnection;

    private final IMessageRouteResolver routeResolver;

    public CsmsProxyImpl(Connection natsConnection, IMessageRouteResolver routingMap) {
        this.natsConnection = natsConnection;
        this.routeResolver = routingMap;
    }

    @Override
    public ICallResultMessage<AuthorizeResponse> sendAuthorizeRequest(ICallMessage<AuthorizeRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req) {
        String requestSubject = routeResolver.getRoute(OCPPMessageType.BootNotificationRequest);
        String responseSubject = routeResolver.getRoute(OCPPMessageType.BootNotificationResponse);

        var requester = new OCPPRequester<>(BootNotificationRequest.class, BootNotificationResponse.class);

        try {
            ICallResultMessage<BootNotificationResponse> response = requester.request(req, requestSubject, responseSubject,
                    natsConnection);

            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ICallResultMessage<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(ICallMessage<ClearedChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(ICallMessage<FirmwareStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(ICallMessage<Get15118EVCertificateRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<GetCertificateStatusResponse> sendGetCertificateStatusRequest(ICallMessage<GetCertificateStatusRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req) {
        var requester = new OCPPRequester<>(StatusNotificationRequest.class, StatusNotificationResponse.class);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.StatusNotificationRequest);
        String responseSubject = routeResolver.getRoute(OCPPMessageType.StatusNotificationResponse);

        try {
            ICallResultMessage<StatusNotificationResponse> response = requester.request(req, requestSubject, responseSubject,
                    natsConnection);

            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ICallResultMessage<TransactionEventResponse> sendTransactionEventRequest(ICallMessage<TransactionEventRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req) {
        var requester = new OCPPRequester<>(HeartbeatRequest.class, HeartbeatResponse.class);

        String requestSubject = routeResolver.getRoute(OCPPMessageType.HeartbeatRequest);
        String responseSubject = routeResolver.getRoute(OCPPMessageType.HeartbeatResponse);

        try {
            ICallResultMessage<HeartbeatResponse> response = requester.request(req, requestSubject, responseSubject,
                    natsConnection);

            return response;
        } catch (OCPPRequestException ex) {
            logger.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ICallResultMessage<LogStatusNotificationResponse> sendLogStatusNotificationRequest(ICallMessage<LogStatusNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(ICallMessage<NotifyChargingLimitRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(ICallMessage<NotifyCustomerInformationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(ICallMessage<NotifyDisplayMessagesRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(ICallMessage<NotifyEVChargingNeedsRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(ICallMessage<NotifyEVChargingScheduleRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyEventResponse> sendNotifyEventRequest(ICallMessage<NotifyEventRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(ICallMessage<NotifyMonitoringReportRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public ICallResultMessage<PublishFirmwareResponse> sendPublishFirmwareRequest(ICallMessage<PublishFirmwareRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(ICallMessage<ReportChargingProfilesRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(ICallMessage<ReservationStatusUpdateRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(ICallMessage<SecurityEventNotificationRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<SignCertificateResponse> sendSignCertificateRequest(ICallMessage<SignCertificateRequest> req) {
        return null;
    }

    @Override
    public IMessageRouteResolver getRouteResolver() {
        return this.routeResolver;
    }
}
