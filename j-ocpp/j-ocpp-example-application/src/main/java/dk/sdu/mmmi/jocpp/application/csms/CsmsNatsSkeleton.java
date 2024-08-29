package dk.sdu.mmmi.jocpp.application.csms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService.HandshakeRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService.HandshakeResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPOverNatsDispatcher;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.SessionInfoImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.managementsystem.ChargingStationNatsIOProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.routes.NatsMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class CsmsNatsSkeleton implements ICsmsServer {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String operatorId;
    private final String csmsId;
    private final Connection natsConnection;
    private final IMessageRouteResolver routeResolver;
    private final ICsms csmsService;
    private final IRequestHandlerRegistry ocppServer;
    private final ISessionManager sessionManager;

    public CsmsNatsSkeleton(BrokerConfig brokerConfig, Options natsOptions, ICsms csms, ISessionManager sessionManager) {
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
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to connect to Nats.IO.", e);
        }
    }

    @Override
    public void serve() {
        addConnectionHandler();

        // TODO: Add handlers for all incoming message types.
        addBootNotificationHandler();
        addStatusNotificationHandler();
        addHeartbeatHandler();
    }

    private void addConnectionHandler() {
        // Connect handler
        Dispatcher dispatcher = natsConnection.createDispatcher(handshakeReq -> {
            String replyTo = handshakeReq.getReplyTo();

            // Deserialize the handshake
            String json = new String(handshakeReq.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received handshake: %s", json));

            ObjectMapper mapper = JacksonUtil.getDefault();

            try {
                HandshakeRequest handshakeRequest = mapper.readValue(json, HandshakeRequestImpl.class);
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
                        handshakeRequest.getIdentity()));
                IOCPPSession session = connect(handshakeRequest);

                NatsMessageRouteResolver routeResolver = new NatsMessageRouteResolver(operatorId, csmsId, handshakeRequest.getIdentity());
                HandshakeResponse accepted = HandshakeResponseImpl.HandshakeResponseImplBuilder.aHandshakeResponseImpl()
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

    private boolean validate(HandshakeRequest handshakeRequest) {
        try {
            Objects.requireNonNull(handshakeRequest.getIdentity());
            Objects.requireNonNull(handshakeRequest.getOcppVersion());
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    private void addBootNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.BootNotificationRequest,
                new OCPPOverNatsIORequestHandler<>(BootNotificationRequest.class, BootNotificationResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.BootNotificationRequest);
                    }

                    @Override
                    public ICallResult<BootNotificationResponse> handle(ICall<BootNotificationRequest> message, String subject) {
                        // Register the charging station within the registry.
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        Headers headers = Headers.emptyHeader();
                        headers.put(Headers.HeaderEnum.CS_ID.getValue(), csId);
                        return csmsService.sendBootNotificationRequest(headers, message);
                    }
                });
    }

    private void addStatusNotificationHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.StatusNotificationRequest,
                new OCPPOverNatsIORequestHandler<>(StatusNotificationRequest.class, StatusNotificationResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.StatusNotificationRequest);
                    }

                    @Override
                    public ICallResult<StatusNotificationResponse> handle(ICall<StatusNotificationRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        Headers headers = Headers.emptyHeader();
                        headers.put(Headers.HeaderEnum.CS_ID.getValue(), csId);
                        return csmsService.sendStatusNotificationRequest(headers, message);
                    }
                });
    }

    private void addHeartbeatHandler() {
        ocppServer.addRequestHandler(OCPPMessageType.HeartbeatRequest,
                new OCPPOverNatsIORequestHandler<>(HeartbeatRequest.class, HeartbeatResponse.class, natsConnection) {
                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(OCPPMessageType.HeartbeatRequest);
                    }

                    @Override
                    public ICallResult<HeartbeatResponse> handle(ICall<HeartbeatRequest> message, String subject) {
                        String[] subjectHierarchy = subject.split("\\.");
                        String csId = subjectHierarchy[5];

                        Headers headers = Headers.emptyHeader();
                        headers.put(Headers.HeaderEnum.CS_ID.getValue(), csId);
                        return csmsService.sendHeartbeatRequest(headers, message);
                    }
                });
    }

    public IOCPPSession connect(HandshakeRequest handshakeRequest) {
        if (sessionManager.sessionExists(handshakeRequest.getIdentity())) {
            logger.warning(String.format("CS with identity %s already connected. Ignoring.", handshakeRequest.getIdentity()));
            return sessionManager.getSession(handshakeRequest.getIdentity());
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
            public ICsms getCsms() {
                return csmsService;
            }

            @Override
            public IChargingStation getChargingStation() {
                return new ChargingStationNatsIOProxy(natsConnection, routeResolver);
            }

            @Override
            public SessionInfo getSessionInfo() {
                SessionInfo sInfo = SessionInfoImpl.SessionInfoImplBuilder.newBuilder()
                        .withCsId(handshakeRequest.getIdentity())
                        .withCsmsId(csmsId)
                        .withConnectionURI(natsConnection.getConnectedUrl())
                        .withTransportType("NATS.io")
                        .withOcppVersion(OcppVersion.OCPP_201)
                        .build();

                return sInfo;
            }
        };

        // Register the session.
        sessionManager.registerSession(handshakeRequest.getIdentity(), ocppSession);

        return ocppSession;
    }
}
