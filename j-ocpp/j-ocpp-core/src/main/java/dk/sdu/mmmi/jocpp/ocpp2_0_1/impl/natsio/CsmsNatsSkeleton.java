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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IHandshakeRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IHandshakeResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionInfoImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.proxies.CsOverNatsIoProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.HandshakeRequestImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.HandshakeResponseImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;

public class CsmsNatsSkeleton {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final String operatorId;
    private final String csmsId;
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;
    private final ICsmsEndpoint csmsService;
    private final IRequestHandlerRegistry ocppServer;
    private final ISessionManager sessionManager;

    public CsmsNatsSkeleton(BrokerConfig brokerConfig, Options natsOptions, ICsmsEndpoint csms, ISessionManager sessionManager) {
        this.natsConnection = getNatsIoConnection(natsOptions);
        this.operatorId = brokerConfig.getOperatorId();
        this.csmsId = brokerConfig.getCsmsId();
        this.routeResolver = new NatsMessageRouteResolver(operatorId, csmsId, "*");
        this.csmsService = csms;
        this.ocppServer = new OCPPOverNatsDispatcher(routeResolver);
        this.sessionManager = sessionManager;
    }

    private Connection getNatsIoConnection(Options natsOptions) {
        try {
            return Nats.connect(natsOptions);
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Failed to connect to Nats.IO.", e);
        }
    }

    public void serve() {
        // Handshake handler (not part of the OCPP specification).
        // The handshake was created as a mechanism to:
        //  (1) Let the CS know if the CSMS is online
        //  (2) Let the CSMS decide whether to accept the subsequent requests from the CS.
        // This behavior is implicitly described in the specification -
        //  - 'OCPP 2.0.1: Part 4 - JSON over WebSockets implementation guide' when using WebSocket as transport.

        addHandshakeHandler();

        // Authorize Request/Response
        addHandler(OCPPMessageType.AuthorizeRequest,
                AuthorizeRequest.class,
                AuthorizeResponse.class,
                csmsService::sendAuthorizeRequest);

        // Boot Notification Request/Response
        addHandler(OCPPMessageType.BootNotificationRequest,
                BootNotificationRequest.class,
                BootNotificationResponse.class,
                csmsService::sendBootNotificationRequest);

        // Cleared Charging Limit Request/Response
        addHandler(OCPPMessageType.ClearedChargingLimitRequest,
                ClearedChargingLimitRequest.class,
                ClearedChargingLimitResponse.class,
                csmsService::sendClearedChargingLimitRequest);

        // Firmware Status Notification Request/Response
        addHandler(OCPPMessageType.FirmwareStatusNotificationRequest,
                FirmwareStatusNotificationRequest.class,
                FirmwareStatusNotificationResponse.class,
                csmsService::sendFirmwareStatusNotificationRequest);

        // Get 15118 EV Certificate Request/Response
        addHandler(OCPPMessageType.Get15118EVCertificateRequest,
                Get15118EVCertificateRequest.class,
                Get15118EVCertificateResponse.class,
                csmsService::sendGet15118EVCertificateRequest);

        // Get Certificate Status Request/Response
        addHandler(OCPPMessageType.GetCertificateStatusRequest,
                GetCertificateStatusRequest.class,
                GetCertificateStatusResponse.class,
                csmsService::sendGetCertificateStatusRequest);

        // Heartbeat Request/Response
        addHandler(OCPPMessageType.HeartbeatRequest,
                HeartbeatRequest.class,
                HeartbeatResponse.class,
                csmsService::sendHeartbeatRequest);

        // Log Status Notification Request/Response
        addHandler(OCPPMessageType.LogStatusNotificationRequest,
                LogStatusNotificationRequest.class,
                LogStatusNotificationResponse.class,
                csmsService::sendLogStatusNotificationRequest);

        // Notify Charging Limit Request/Response
        addHandler(OCPPMessageType.NotifyChargingLimitRequest,
                NotifyChargingLimitRequest.class,
                NotifyChargingLimitResponse.class,
                csmsService::sendNotifyChargingLimitRequest);

        // Notify Customer Information Request/Response
        addHandler(OCPPMessageType.NotifyCustomerInformationRequest,
                NotifyCustomerInformationRequest.class,
                NotifyCustomerInformationResponse.class,
                csmsService::sendNotifyCustomerInformationRequest);

        // Notify Display Messages Request/Response
        addHandler(OCPPMessageType.NotifyDisplayMessagesRequest,
                NotifyDisplayMessagesRequest.class,
                NotifyDisplayMessagesResponse.class,
                csmsService::sendNotifyDisplayMessagesRequest);

        // Notify EV Charging Needs Request/Response
        addHandler(OCPPMessageType.NotifyEVChargingNeedsRequest,
                NotifyEVChargingNeedsRequest.class,
                NotifyEVChargingNeedsResponse.class,
                csmsService::sendNotifyEVChargingNeedsRequest);

        // Notify EV Charging Schedule Request/Response
        addHandler(OCPPMessageType.NotifyEVChargingScheduleRequest,
                NotifyEVChargingScheduleRequest.class,
                NotifyEVChargingScheduleResponse.class,
                csmsService::sendNotifyEVChargingScheduleRequest);

        // Notify Event Request/Response
        addHandler(OCPPMessageType.NotifyEventRequest,
                NotifyEventRequest.class,
                NotifyEventResponse.class,
                csmsService::sendNotifyEventRequest);

        // Notify Monitoring Report Request/Response
        addHandler(OCPPMessageType.NotifyMonitoringReportRequest,
                NotifyMonitoringReportRequest.class,
                NotifyMonitoringReportResponse.class,
                csmsService::sendNotifyMonitoringReportRequest);

        // Notify Report Request/Response
        addHandler(OCPPMessageType.NotifyReportRequest,
                NotifyReportRequest.class,
                NotifyReportResponse.class,
                csmsService::sendNotifyReportRequest);

        // Publish Firmware Request/Response
        addHandler(OCPPMessageType.PublishFirmwareRequest,
                PublishFirmwareRequest.class,
                PublishFirmwareResponse.class,
                csmsService::sendPublishFirmwareRequest);

        // Report Charging Profiles Request/Response
        addHandler(OCPPMessageType.ReportChargingProfilesRequest,
                ReportChargingProfilesRequest.class,
                ReportChargingProfilesResponse.class,
                csmsService::sendReportChargingProfilesRequest);

        // Reservation Status Update Request/Response
        addHandler(OCPPMessageType.ReservationStatusUpdateRequest,
                ReservationStatusUpdateRequest.class,
                ReservationStatusUpdateResponse.class,
                csmsService::sendReservationStatusUpdateRequest);

        // Security Event Notification Request/Response
        addHandler(OCPPMessageType.SecurityEventNotificationRequest,
                SecurityEventNotificationRequest.class,
                SecurityEventNotificationResponse.class,
                csmsService::sendSecurityEventNotificationRequest);

        // Sign Certificate Request/Response
        addHandler(OCPPMessageType.SignCertificateRequest,
                SignCertificateRequest.class,
                SignCertificateResponse.class,
                csmsService::sendSignCertificateRequest);

        // Status Notification Request/Response
        addHandler(OCPPMessageType.StatusNotificationRequest,
                StatusNotificationRequest.class,
                StatusNotificationResponse.class,
                csmsService::sendStatusNotificationRequest);

        // Transaction Event Request/Response
        addHandler(OCPPMessageType.TransactionEventRequest,
                TransactionEventRequest.class,
                TransactionEventResponse.class,
                csmsService::sendTransactionEventRequest);
    }

    private void addHandshakeHandler() {
        // Connect handler
        Dispatcher dispatcher = natsConnection.createDispatcher(handshakeReq -> {
            String replyTo = handshakeReq.getReplyTo();

            // Deserialize the handshake
            String json = new String(handshakeReq.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received handshake: %s", json));

            ObjectMapper mapper = JacksonUtil.getDefault();

            try {
                IHandshakeRequest handshakeRequest = mapper.readValue(json, HandshakeRequestImpl.class);
                /*
                 * Handshake validation
                 */
                if (!validate(handshakeRequest)) {
                    HandshakeResponseImpl rejected = HandshakeResponseImpl.HandshakeResponseImplBuilder.aHandshakeResponseImpl()
                            .withHandshakeResult(HandshakeResult.REJECTED)
                            .withReason("Invalid handshake request.")
                            .build();

                    String jsonResponse = null;
                    try {
                        jsonResponse = mapper.writerFor(HandshakeResponseImpl.class).writeValueAsString(rejected);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                    logger.info(String.format("Invalid handshake. Sending response: %s", jsonResponse));
                    natsConnection.publish(replyTo, jsonResponse.getBytes(StandardCharsets.UTF_8));
                }

                /*
                 * Create the endpoint and add it to the internal connections.
                 */
                logger.info(String.format("Handshake accepted. Instantiating endpoint for CS: %s",
                        handshakeRequest.getCsIdentity()));
                IOCPPSession session = connect(handshakeRequest);

                NatsMessageRouteResolver routeResolver = new NatsMessageRouteResolver(operatorId, csmsId, handshakeRequest.getCsIdentity());
                IHandshakeResponse accepted = HandshakeResponseImpl.HandshakeResponseImplBuilder.aHandshakeResponseImpl()
                        .withHandshakeResult(HandshakeResult.ACCEPTED)
                        .withEndpoint(routeResolver.getRequestRoute())
                        .withOcppVersion(OcppVersion.OCPP_201)
                        .build();

                String jsonResponse = mapper.writeValueAsString(accepted);
                logger.info(String.format("Sending handshake response: %s", jsonResponse));
                natsConnection.publish(replyTo, jsonResponse.getBytes(StandardCharsets.UTF_8));

            } catch (JsonProcessingException e) {
                HandshakeResponseImpl rejected = HandshakeResponseImpl.HandshakeResponseImplBuilder.aHandshakeResponseImpl()
                        .withHandshakeResult(HandshakeResult.REJECTED)
                        .withReason("An error occurred during handshake.")
                        .build();

                String jsonResponse = null;
                try {
                    jsonResponse = mapper.writerFor(HandshakeResponseImpl.class).writeValueAsString(rejected);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                natsConnection.publish(replyTo, jsonResponse.getBytes(StandardCharsets.UTF_8));
            }
        });

        // Subscribe to the connection subject to handle the handshake request.
        String operatorId = this.operatorId.replace(" ", "").toLowerCase();
        String csmsId = this.csmsId.replace(" ", "").toLowerCase();
        String connectSubject = String.format("operators.%s.csms.%s.connect", operatorId, csmsId);
        logger.info(String.format("Added connect handler on subject: %s", connectSubject));
        dispatcher.subscribe(connectSubject);
    }

    private boolean validate(IHandshakeRequest handshakeRequest) {
        try {
            Objects.requireNonNull(handshakeRequest.getCsIdentity());
            Objects.requireNonNull(handshakeRequest.getOcppVersion());
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    /**
     * Generic handler method for registering OCPP over NATS.io
     * @param messageType
     * @param requestClass
     * @param responseClass
     * @param serviceMethod
     * @param <TRequest>
     * @param <TResponse>
     */
    private <TRequest, TResponse> void addHandler(
            OCPPMessageType messageType,
            Class<TRequest> requestClass,
            Class<TResponse> responseClass,
            BiFunction<Headers, ICall<TRequest>, ICallResult<TResponse>> serviceMethod) {

        ocppServer.addRequestHandler(messageType,
                new OCPPOverNatsIORequestHandler<>(requestClass, responseClass, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(messageType);
                    }

                    @Override
                    public ICallResult<TResponse> handle(ICall<TRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        Headers headers = Headers.emptyHeader();
                        headers.put(Headers.HeaderEnum.CS_ID.getValue(), csId);

                        return serviceMethod.apply(headers, message);
                    }
                });
    }

    private IOCPPSession connect(IHandshakeRequest handshakeRequest) {
        String csIdentity = handshakeRequest.getCsIdentity();
        if (sessionManager.sessionExists(csIdentity)) {
            logger.warn(String.format("CS with identity %s already connected. Ignoring.", csIdentity));
            return sessionManager.getSession(csIdentity);
        }


        /*
         * Create session object.
         */
        IOCPPSession ocppSession = new IOCPPSession() {
            @Override
            public void disconnect() {
                // Nothing to do here, because the NATS.io dispatcher serves all incoming requests.
            }

            @Override
            public ICsmsEndpoint getCsms() {
                return csmsService;
            }

            @Override
            public ICsEndpoint getChargingStation() {
                IMessageRouteResolver proxyRouteResolver = new NatsMessageRouteResolver(operatorId, csmsId, csIdentity);
                return new CsOverNatsIoProxy(natsConnection, proxyRouteResolver);
            }

            @Override
            public SessionInfo getSessionInfo() {
                SessionInfo sInfo = SessionInfoImpl.SessionInfoImplBuilder.newBuilder()
                        .withCsId(handshakeRequest.getCsIdentity())
                        .withCsmsId(csmsId)
                        .withConnectionURI(natsConnection.getConnectedUrl())
                        .withTransportType("NATS.io")
                        .withOcppVersion(OcppVersion.OCPP_201)
                        .build();

                return sInfo;
            }
        };

        // Register the session.
        sessionManager.registerSession(csIdentity, ocppSession);

        return ocppSession;
    }
}
