package dk.sdu.mmmi.jocpp.ocpp2_0_1.integration.natsio;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.OCPPOverNatsDispatcher;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.proxies.CsmsEndpointOverNatsIoProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerContextLoader;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChargingStationClientTest {
    private static final String CS_ID = "ce2b8b0e-db26-4643-a705-c848fab64327";

    private static final String CSMS_ID = "Clever CSMS";
    private IRequestHandlerRegistry csService;

    @BeforeEach
    void setup_and_connect_csms_to_nats() {
        URL resource = getResource("BrokerContexts/brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        String brokerUrl = brokerContext.getConfigFromCsId(CS_ID).getBrokerUrl();
        Connection natsConnection = getNatsConnection(brokerUrl);
        IMessageRouteResolver routeResolver = brokerContext.getCsmsRouteResolver(CSMS_ID);

        BootNotificationRequestHandler bnrHandler = new BootNotificationRequestHandler(BootNotificationRequest.class, BootNotificationResponse.class, natsConnection);
        bnrHandler.setMessageRouteResolver(routeResolver);

        StatusNotificationRequestHandler snrHandler = new StatusNotificationRequestHandler(StatusNotificationRequest.class, StatusNotificationResponse.class, natsConnection);
        snrHandler.setMessageRouteResolver(routeResolver);

        HeartbeatRequestHandler hbrHandler = new HeartbeatRequestHandler(HeartbeatRequest.class, HeartbeatResponse.class, natsConnection);
        hbrHandler.setMessageRouteResolver(routeResolver);

        csService = new OCPPOverNatsDispatcher(routeResolver);
        csService.addRequestHandler(OCPPMessageType.BootNotificationRequest, bnrHandler);
        csService.addRequestHandler(OCPPMessageType.StatusNotificationRequest, snrHandler);
        csService.addRequestHandler(OCPPMessageType.HeartbeatRequest, hbrHandler);
    }

    private static URL getResource(String resourceFile) {
        ClassLoader classLoader = ChargingStationServiceTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceFile);

        if (resourceUrl == null) {
            fail(String.format("Could not read input resource file: %s. Ensure that the file exists.", resourceFile));
        }

        return resourceUrl;
    }

    @Test
    void integration_cs_to_csms_BootNotificationRequest() {
        URL resource = getResource("BrokerContexts/brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        BrokerConfig csBrokerConfig = brokerContext.getConfigFromCsId(CS_ID);

        Connection natsConnection = getNatsConnection(csBrokerConfig.getBrokerUrl());

        IMessageRouteResolver routeResolver = brokerContext.getChargingStationRouteResolver(CS_ID);

        ICsmsEndpoint csClient = new CsmsEndpointOverNatsIoProxy(natsConnection, routeResolver);

        ICall<BootNotificationRequest> bootNotificationRequest = createBootNotificationRequest();

        Headers headers = Headers.emptyHeader();
        ICallResult<BootNotificationResponse> bootNotificationResponse =
                csClient.sendBootNotificationRequest(headers, bootNotificationRequest);

        if (bootNotificationResponse == null) {
            fail("Received 'null' response.");
        }

        // MessageId's of request/response must be equal for tracability.
        assertEquals(bootNotificationRequest.getMessageId(), bootNotificationResponse.getMessageId());
        // Expect a call result.
        assertEquals(MessageType.CALLRESULT, bootNotificationResponse.getMessageTypeId());
        // Expect that the boot is accepted by default.
        assertEquals(RegistrationStatusEnum.ACCEPTED, bootNotificationResponse.getPayload().getStatus());
    }

    private static Connection getNatsConnection(String natsUrl) {
        Connection natsConnection = null;
        try {
            natsConnection = Nats.connect(Options.builder()
                    .server(natsUrl)
                    .build());
        } catch (IOException | InterruptedException cause) {
            fail("Failed to connect to a NATS.IO server", cause);
        }
        return natsConnection;
    }

    @Test
    void integration_cs_to_csms_StatusNotificationRequest() {
        URL resource = getResource("BrokerContexts/brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        BrokerConfig csBrokerConfig = brokerContext.getConfigFromCsId(CS_ID);

        Connection natsConnection = getNatsConnection(csBrokerConfig.getBrokerUrl());

        IMessageRouteResolver routeResolver = brokerContext.getChargingStationRouteResolver(CS_ID);

        ICsmsEndpoint csClient = new CsmsEndpointOverNatsIoProxy(natsConnection, routeResolver);

        ICall<StatusNotificationRequest> statusNotificationRequest = createStatusNotificationRequest();

        Headers headers = Headers.emptyHeader();
        ICallResult<StatusNotificationResponse> statusNotificationResponse =
                csClient.sendStatusNotificationRequest(headers, statusNotificationRequest);

        if (statusNotificationResponse == null) {
            fail("Received 'null' response.");
        }

        // MessageId's of request/response must be equal for tracability.
        assertEquals(statusNotificationRequest.getMessageId(), statusNotificationResponse.getMessageId());
        // Expect a call result.
        assertEquals(MessageType.CALLRESULT, statusNotificationResponse.getMessageTypeId());
    }

    @Test
    void integration_cs_to_csms_HeartbeatRequest() {
        URL resource = getResource("BrokerContexts/brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        BrokerConfig csBrokerConfig = brokerContext.getConfigFromCsId(CS_ID);

        Connection natsConnection = getNatsConnection(csBrokerConfig.getBrokerUrl());

        IMessageRouteResolver routeResolver = brokerContext.getChargingStationRouteResolver(CS_ID);

        ICsmsEndpoint csClient = new CsmsEndpointOverNatsIoProxy(natsConnection, routeResolver);

        ICall<HeartbeatRequest> heartbeatRequest = createHeartbeatRequest();

        Headers headers = Headers.emptyHeader();
        ICallResult<HeartbeatResponse> heartbeatResponse =
                csClient.sendHeartbeatRequest(headers, heartbeatRequest);

        // MessageId's of request/response must be equal for tracability.
        assertEquals(heartbeatRequest.getMessageId(), heartbeatResponse.getMessageId());
        // Expect a call result.
        assertEquals(MessageType.CALLRESULT, heartbeatResponse.getMessageTypeId());
    }


    private static ICall<BootNotificationRequest> createBootNotificationRequest() {
        BootNotificationRequest bootNotificationRequest = new BootNotificationRequest.BootNotificationRequestBuilder()
                .withReason(BootReasonEnum.POWER_UP)
                .withChargingStation(
                        new ChargingStation.ChargingStationBuilder()
                                .withModel("SingleSocketCharger")
                                .withVendorName("VendorX")
                                .build())
                .build();

        ICall<BootNotificationRequest> callMessage = CallImpl.<BootNotificationRequest>newBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .asAction(OCPPMessageType.BootNotificationRequest.getAction())
                .withPayLoad(bootNotificationRequest)
                .build();

        return callMessage;
    }

    private static ICall<StatusNotificationRequest> createStatusNotificationRequest() {
        StatusNotificationRequest statusNotificationRequest = new StatusNotificationRequest.StatusNotificationRequestBuilder()
                .withEvseId(0)
                .withConnectorId(0)
                .withTimestamp(ZonedDateTime.now(Clock.systemDefaultZone()))
                .withConnectorStatus(ConnectorStatusEnum.OCCUPIED)
                .build();

        ICall<StatusNotificationRequest> callMessage = CallImpl.<StatusNotificationRequest>newBuilder()
                .withMessageId("53d9c081-aa03-4456-a5f4-811fc870f0bd")
                .asAction(OCPPMessageType.StatusNotificationRequest.getAction())
                .withPayLoad(statusNotificationRequest)
                .build();

        return callMessage;
    }

    private static ICall<HeartbeatRequest> createHeartbeatRequest() {
        HeartbeatRequest heartbeatRequest = HeartbeatRequest.builder().build();

        ICall<HeartbeatRequest> callMessage = CallImpl.<HeartbeatRequest>newBuilder()
                .withMessageId("3211b8d5-16aa-4dec-877a-9a9b114e86b3")
                .asAction(OCPPMessageType.HeartbeatRequest.getAction())
                .withPayLoad(heartbeatRequest)
                .build();

        return callMessage;
    }

    static class BootNotificationRequestHandler extends OCPPOverNatsIORequestHandler<BootNotificationRequest, BootNotificationResponse> {
        private IMessageRouteResolver messageRouteResolver;

        /**
         * Instantiate a new OCPPRequestHandler with the INBOUND and OUTBOUND payload types.
         *
         * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
         * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
         */
        public BootNotificationRequestHandler(Class<BootNotificationRequest> inPayloadType, Class<BootNotificationResponse> outPayloadType, Connection natsConnection) {
            super(inPayloadType, outPayloadType, natsConnection);
        }

        public void setMessageRouteResolver(IMessageRouteResolver resolver) {
            this.messageRouteResolver = resolver;
        }

        @Override
        public ICallResult<BootNotificationResponse> handle(ICall<BootNotificationRequest> message, String subject) {
            // Register the charging station within the registry.
            String[] subjectHierarchy = subject.split("\\.");
            String csId = subjectHierarchy[5];

            BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                    .withStatus(RegistrationStatusEnum.ACCEPTED)
                    .withCurrentTime(ZonedDateTime.now(Clock.systemDefaultZone()))
                    .withInterval((int) Duration.ofMinutes(2).toSeconds())
                    .build();

            ICallResult<BootNotificationResponse> callResultMessage =
                    CallResultImpl.<BootNotificationResponse>newBuilder()
                            .withMessageId(message.getMessageId()) // NB! Important to reuse the message ID for traceability
                            .withPayLoad(bootNotificationResponse)
                            .build();

            return callResultMessage;
        }

        @Override
        public String getRequestSubject() {
            return messageRouteResolver.getRoute(OCPPMessageType.BootNotificationRequest);
        }
    }

    static class StatusNotificationRequestHandler extends OCPPOverNatsIORequestHandler<StatusNotificationRequest, StatusNotificationResponse> {
        private IMessageRouteResolver messageRouteResolver;

        /**
         * Instantiate a new OCPPRequestHandler with the INBOUND and OUTBOUND payload types.
         *
         * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
         * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
         */
        public StatusNotificationRequestHandler(Class<StatusNotificationRequest> inPayloadType, Class<StatusNotificationResponse> outPayloadType, Connection natsConnection) {
            super(inPayloadType, outPayloadType, natsConnection);
        }

        public void setMessageRouteResolver(IMessageRouteResolver resolver) {
            this.messageRouteResolver = resolver;
        }

        @Override
        public ICallResult<StatusNotificationResponse> handle(ICall<StatusNotificationRequest> message, String subject) {
            String[] subjectHierarchy = subject.split("\\.");
            String csId = subjectHierarchy[5];

            StatusNotificationRequest statusNotificationRequest = message.getPayload();

            StatusNotificationResponse statusNotificationResponse = StatusNotificationResponse.builder()
                    // No fields required as specified in OCPP 2.0.1.
                    .build();

            ICallResult<StatusNotificationResponse> callResultMessage =
                    CallResultImpl.<StatusNotificationResponse>newBuilder()
                            .withMessageId(message.getMessageId()) // NB! Important to reuse the message ID for traceability
                            .withPayLoad(statusNotificationResponse)
                            .build();

            return callResultMessage;
        }

        @Override
        public String getRequestSubject() {
            return messageRouteResolver.getRoute(OCPPMessageType.StatusNotificationRequest);
        }
    }

    static class HeartbeatRequestHandler extends OCPPOverNatsIORequestHandler<HeartbeatRequest, HeartbeatResponse> {
        private IMessageRouteResolver messageRouteResolver;

        /**
         * Instantiate a new OCPPRequestHandler with the INBOUND and OUTBOUND payload types.
         *
         * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
         * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
         */
        public HeartbeatRequestHandler(Class<HeartbeatRequest> inPayloadType, Class<HeartbeatResponse> outPayloadType, Connection natsConnection) {
            super(inPayloadType, outPayloadType, natsConnection);
        }

        public void setMessageRouteResolver(IMessageRouteResolver resolver) {
            this.messageRouteResolver = resolver;
        }

        @Override
        public ICallResult<HeartbeatResponse> handle(ICall<HeartbeatRequest> message, String subject) {
            String[] subjectHierarchy = subject.split("\\.");
            String csId = subjectHierarchy[5];

            HeartbeatRequest heartbeatRequest = message.getPayload();

            HeartbeatResponse response = HeartbeatResponse.builder()
                    .withCurrentTime(ZonedDateTime.now(Clock.systemDefaultZone()))
                    .build();

            ICallResult<HeartbeatResponse> callResultMessage =
                    CallResultImpl.<HeartbeatResponse>newBuilder()
                            .withMessageId(message.getMessageId()) // NB! Important to reuse the message ID for traceability
                            .withPayLoad(response)
                            .build();

            return callResultMessage;
        }

        @Override
        public String getRequestSubject() {
            return messageRouteResolver.getRoute(OCPPMessageType.HeartbeatRequest);
        }
    }
}
