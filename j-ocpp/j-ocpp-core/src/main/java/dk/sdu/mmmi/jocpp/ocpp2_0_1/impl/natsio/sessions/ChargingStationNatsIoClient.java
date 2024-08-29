package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.sessions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.OCPPOverNatsDispatcher;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.proxies.CsmsOverNatsIoProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.HandshakeRequestImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.HandshakeResponseImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionInfoImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ChargingStationNatsIoClient implements IOCPPSession {
    private final IMessageRouteResolver routeResolver;
    private final SessionInfoImpl sessionInfo;
    private ICsms csmsProxy;
    private IRequestHandlerRegistry requestDispatchers;
    private IChargingStation csService;
    private Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationNatsIoClient.class.getName());
    private final Options natsOptions;

    public ChargingStationNatsIoClient(IChargingStation csService,
                                       IMessageRouteResolver routeResolver,
                                       Options natsOptions) {
        this.csService = csService;
        this.routeResolver = routeResolver;
        this.natsOptions = natsOptions;
        this.requestDispatchers = new OCPPOverNatsDispatcher(routeResolver);
        this.sessionInfo = SessionInfoImpl.SessionInfoImplBuilder.newBuilder()
                    .withCsId(routeResolver.getCsIdentity())
                    .withCsmsId(routeResolver.getCsmsIdentity())
                    .withOcppVersion(OcppVersion.OCPP_201)
                    .withTransportType("NATS.io")
                    .withConnectionURI(natsOptions.getServers().get(0).toString())
                .build();
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    private void connect() {
        try {
            this.natsConnection = Nats.connect(this.natsOptions);
            initRequestDispatchers();

            this.csmsProxy = connect(HandshakeRequestImpl.HandshakeInfoImplBuilder.newBuilder()
                    .withIdentity(routeResolver.getCsIdentity())
                    .withOcppVersion(OcppVersion.OCPP_201)
                    .build());

        } catch (IOException | InterruptedException e ) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void initRequestDispatchers() {
        // Step 1: Create a mapping between OCPPMessageType and their corresponding request/response classes
        final Map<OCPPMessageType, Class<?>[]> ocppMessageMap = new HashMap<>() {{
            put(OCPPMessageType.CancelReservationRequest, new Class[]{CancelReservationRequest.class, CancelReservationResponse.class});
            put(OCPPMessageType.CertificateSignedRequest, new Class[]{CertificateSignedRequest.class, CertificateSignedResponse.class});
            put(OCPPMessageType.ChangeAvailabilityRequest, new Class[]{ChangeAvailabilityRequest.class, ChangeAvailabilityResponse.class});
            put(OCPPMessageType.ClearCacheRequest, new Class[]{ClearCacheRequest.class, ClearCacheResponse.class});
            put(OCPPMessageType.ClearChargingProfileRequest, new Class[]{ClearChargingProfileRequest.class, ClearChargingProfileResponse.class});
            put(OCPPMessageType.ClearDisplayMessageRequest, new Class[]{ClearDisplayMessageRequest.class, ClearDisplayMessageResponse.class});
            put(OCPPMessageType.ClearVariableMonitoringRequest, new Class[]{ClearVariableMonitoringRequest.class, ClearVariableMonitoringResponse.class});
            put(OCPPMessageType.CostUpdatedRequest, new Class[]{CostUpdatedRequest.class, CostUpdatedResponse.class});
            put(OCPPMessageType.CustomerInformationRequest, new Class[]{CustomerInformationRequest.class, CustomerInformationResponse.class});
            put(OCPPMessageType.DeleteCertificateRequest, new Class[]{DeleteCertificateRequest.class, DeleteCertificateResponse.class});
            put(OCPPMessageType.GetBaseReportRequest, new Class[]{GetBaseReportRequest.class, GetBaseReportResponse.class});
            put(OCPPMessageType.GetChargingProfilesRequest, new Class[]{GetChargingProfilesRequest.class, GetChargingProfilesResponse.class});
            put(OCPPMessageType.GetCompositeScheduleRequest, new Class[]{GetCompositeScheduleRequest.class, GetCompositeScheduleResponse.class});
            put(OCPPMessageType.GetDisplayMessagesRequest, new Class[]{GetDisplayMessagesRequest.class, GetDisplayMessagesResponse.class});
            put(OCPPMessageType.GetInstalledCertificateIdsRequest, new Class[]{GetInstalledCertificateIdsRequest.class, GetInstalledCertificateIdsResponse.class});
            put(OCPPMessageType.GetLocalListVersionRequest, new Class[]{GetLocalListVersionRequest.class, GetLocalListVersionResponse.class});
            put(OCPPMessageType.GetLogRequest, new Class[]{GetLogRequest.class, GetLogResponse.class});
            put(OCPPMessageType.GetMonitoringReportRequest, new Class[]{GetMonitoringReportRequest.class, GetMonitoringReportResponse.class});
            put(OCPPMessageType.GetReportRequest, new Class[]{GetReportRequest.class, GetReportResponse.class});
            put(OCPPMessageType.GetTransactionStatusRequest, new Class[]{GetTransactionStatusRequest.class, GetTransactionStatusResponse.class});
            put(OCPPMessageType.GetVariablesRequest, new Class[]{GetVariablesRequest.class, GetVariablesResponse.class});
            put(OCPPMessageType.InstallCertificateRequest, new Class[]{InstallCertificateRequest.class, InstallCertificateResponse.class});
            put(OCPPMessageType.MeterValuesRequest, new Class[]{MeterValuesRequest.class, MeterValuesResponse.class});
            put(OCPPMessageType.RequestStartTransactionRequest, new Class[]{RequestStartTransactionRequest.class, RequestStartTransactionResponse.class});
            put(OCPPMessageType.RequestStopTransactionRequest, new Class[]{RequestStopTransactionRequest.class, RequestStopTransactionResponse.class});
            put(OCPPMessageType.ReserveNowRequest, new Class[]{ReserveNowRequest.class, ReserveNowResponse.class});
            put(OCPPMessageType.ResetRequest, new Class[]{ResetRequest.class, ResetResponse.class});
            put(OCPPMessageType.SendLocalListRequest, new Class[]{SendLocalListRequest.class, SendLocalListResponse.class});
            put(OCPPMessageType.SetChargingProfileRequest, new Class[]{SetChargingProfileRequest.class, SetChargingProfileResponse.class});
            put(OCPPMessageType.SetDisplayMessageRequest, new Class[]{SetDisplayMessageRequest.class, SetDisplayMessageResponse.class});
            put(OCPPMessageType.SetMonitoringBaseRequest, new Class[]{SetMonitoringBaseRequest.class, SetMonitoringBaseResponse.class});
            put(OCPPMessageType.SetMonitoringLevelRequest, new Class[]{SetMonitoringLevelRequest.class, SetMonitoringLevelResponse.class});
            put(OCPPMessageType.SetNetworkProfileRequest, new Class[]{SetNetworkProfileRequest.class, SetNetworkProfileResponse.class});
            put(OCPPMessageType.SetVariableMonitoringRequest, new Class[]{SetVariableMonitoringRequest.class, SetVariableMonitoringResponse.class});
            put(OCPPMessageType.SetVariablesRequest, new Class[]{SetVariablesRequest.class, SetVariablesResponse.class});
            put(OCPPMessageType.TriggerMessageRequest, new Class[]{TriggerMessageRequest.class, TriggerMessageResponse.class});
            put(OCPPMessageType.UnlockConnectorRequest, new Class[]{UnlockConnectorRequest.class, UnlockConnectorResponse.class});
            put(OCPPMessageType.UnpublishFirmwareRequest, new Class[]{UnpublishFirmwareRequest.class, UnpublishFirmwareResponse.class});
            put(OCPPMessageType.UpdateFirmwareRequest, new Class[]{UpdateFirmwareRequest.class, UpdateFirmwareResponse.class});
        }};

        // Method map to store the methods for each OCPPMessageType
        final Map<OCPPMessageType, Method> methodMap = new HashMap<>();

        // Initialize methodMap with methods from CsServiceEndpoint
        try {
            Class<?> endpointClass = IChargingStation.class;
            methodMap.put(OCPPMessageType.CancelReservationRequest, endpointClass.getMethod("sendCancelReservationRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.CertificateSignedRequest, endpointClass.getMethod("sendCertificateSignedRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ChangeAvailabilityRequest, endpointClass.getMethod("sendChangeAvailabilityRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ClearCacheRequest, endpointClass.getMethod("sendClearCacheRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ClearChargingProfileRequest, endpointClass.getMethod("sendClearChargingProfileRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ClearDisplayMessageRequest, endpointClass.getMethod("sendClearDisplayMessageRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ClearVariableMonitoringRequest, endpointClass.getMethod("sendClearVariableMonitoringRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.CostUpdatedRequest, endpointClass.getMethod("sendCostUpdatedRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.CustomerInformationRequest, endpointClass.getMethod("sendCustomerInformationRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.DeleteCertificateRequest, endpointClass.getMethod("sendDeleteCertificateRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetBaseReportRequest, endpointClass.getMethod("sendGetBaseReportRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetChargingProfilesRequest, endpointClass.getMethod("sendGetChargingProfilesRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetCompositeScheduleRequest, endpointClass.getMethod("sendGetCompositeScheduleRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetDisplayMessagesRequest, endpointClass.getMethod("sendGetDisplayMessagesRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetInstalledCertificateIdsRequest, endpointClass.getMethod("sendGetInstalledCertificateIdsRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetLocalListVersionRequest, endpointClass.getMethod("sendGetLocalListVersionRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetLogRequest, endpointClass.getMethod("sendGetLogRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetMonitoringReportRequest, endpointClass.getMethod("sendGetMonitoringReportRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetReportRequest, endpointClass.getMethod("sendGetReportRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetTransactionStatusRequest, endpointClass.getMethod("sendGetTransactionStatusRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.GetVariablesRequest, endpointClass.getMethod("sendGetVariablesRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.InstallCertificateRequest, endpointClass.getMethod("sendInstallCertificateRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.MeterValuesRequest, endpointClass.getMethod("sendMeterValuesRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.RequestStartTransactionRequest, endpointClass.getMethod("sendRequestStartTransactionRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.RequestStopTransactionRequest, endpointClass.getMethod("sendRequestStopTransactionRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ReserveNowRequest, endpointClass.getMethod("sendReserveNowRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.ResetRequest, endpointClass.getMethod("sendResetRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SendLocalListRequest, endpointClass.getMethod("sendSendLocalListRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetChargingProfileRequest, endpointClass.getMethod("sendSetChargingProfileRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetDisplayMessageRequest, endpointClass.getMethod("sendSetDisplayMessageRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetMonitoringBaseRequest, endpointClass.getMethod("sendSetMonitoringBaseRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetMonitoringLevelRequest, endpointClass.getMethod("sendSetMonitoringLevelRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetNetworkProfileRequest, endpointClass.getMethod("sendSetNetworkProfileRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetVariableMonitoringRequest, endpointClass.getMethod("sendSetVariableMonitoringRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.SetVariablesRequest, endpointClass.getMethod("sendSetVariablesRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.TriggerMessageRequest, endpointClass.getMethod("sendTriggerMessageRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.UnlockConnectorRequest, endpointClass.getMethod("sendUnlockConnectorRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.UnpublishFirmwareRequest, endpointClass.getMethod("sendUnpublishFirmwareRequest", Headers.class, ICall.class));
            methodMap.put(OCPPMessageType.UpdateFirmwareRequest, endpointClass.getMethod("sendUpdateFirmwareRequest", Headers.class, ICall.class));
        }
        catch(NoSuchMethodException ex) {
            logger.severe(ex.toString());
        }

        // Register each NATS.io dispatcher with the concrete interface method.
        ocppMessageMap.forEach((messageType, classes) -> {
            Class<?> requestClass = classes[0];
            Class<?> responseClass = classes[1];
            registerHandler(messageType, requestClass, responseClass, methodMap, this.csService);
        });
    }

    private <TRequest, TResponse> void registerHandler(OCPPMessageType messageType, Class<TRequest> requestClass,
                                                       Class<TResponse> responseClass,
                                                       Map<OCPPMessageType, Method> methodMap,
                                                       IChargingStation csService) {
        requestDispatchers.addRequestHandler(messageType,
                new OCPPOverNatsIORequestHandler<>(requestClass, responseClass, natsConnection) {
                    @Override
                    public ICallResult<TResponse> handle(ICall<TRequest> message, String subject) {
                        // Lookup the method and invoke it.
                        Method method = methodMap.get(messageType);
                        try {
                            Map<String, String> headers = Collections.emptyMap();
                            return (ICallResult<TResponse>) method.invoke(csService, headers, message);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public String getRequestSubject() {
                        return routeResolver.getRoute(messageType);
                    }
                });
    }


    /**
     * This operation is NOT part of OCPP 2.0.1.
     *
     * This operation returns a message routing map that can be used to look up the route for a specific message type.
     * @return
     */
    public IMessageRouteResolver getRouteResolver() {
        return this.routeResolver;
    }

    public ICsms connect(IHandshakeRequest handshakeRequest) {
        String connectSubject = routeResolver.getConnectRoute();

        try {
            // Serialize the handshake
            ObjectMapper mapper = JacksonUtil.getDefault();
            String jsonPayload = mapper.writeValueAsString(handshakeRequest);

            // Send the handshake
            logger.info(String.format("Sending handshake on subject %s : %s", connectSubject,
                    jsonPayload));
            CompletableFuture<Message> futureResponse =
                    natsConnection.requestWithTimeout(connectSubject,
                            jsonPayload.getBytes(StandardCharsets.UTF_8), Duration.ofSeconds(30));

            // Get the handshake response
            Message msgResponse = futureResponse.get();
            String respJsonPayload = new String(msgResponse.getData(), StandardCharsets.UTF_8);

            IHandshakeResponse handshakeResponse = mapper.readValue(respJsonPayload, HandshakeResponseImpl.class);
            logger.info(String.format("Handshake response: %s", respJsonPayload));

            return new CsmsOverNatsIoProxy(natsConnection, routeResolver);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChargingStationNatsIOClientBuilder newBuilder() {
        return new ChargingStationNatsIOClientBuilder();
    }

    @Override
    public void disconnect() {
        try {
            natsConnection.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ICsms getCsms() {
        return this.csmsProxy;
    }

    @Override
    public IChargingStation getChargingStation() {
        return csService;
    }

    @Override
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public static class ChargingStationNatsIOClientBuilder {
        private String csId;
        private IBrokerContext configs;
        private IChargingStation csService;

        public ChargingStationNatsIOClientBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationNatsIOClientBuilder withCsServiceInterface(IChargingStation csService) {
            this.csService = csService;
            return this;
        }

        public ChargingStationNatsIOClientBuilder withBrokerContext(IBrokerContext configs) {
            this.configs = configs;
            return this;
        }

        public ChargingStationNatsIoClient build() {
            if (csId == null) throw new IllegalArgumentException("Charging Station ID must not be null. Provide a Charging Station Id.");
            if (configs == null) throw new IllegalArgumentException("BrokerContext must not be null. Provide a BrokerContext.");

            BrokerConfig csBrokerConfig = configs.getConfigFromCsId(csId);

            Options natsOptions = Options.builder()
                    .server(csBrokerConfig.getBrokerUrl())
                    .connectionName(String.format("Charging Station Server operatorId=%s csmsId=%s csId=%s",
                            csBrokerConfig.getOperatorId(), csBrokerConfig.getCsmsId(), csId))
                    .connectionTimeout(Duration.ofMinutes(2))
                    .connectionListener((connection, eventType) -> {
                        logger.info(String.format("NATS.io connection event: %s%n", eventType));
                    })
                    .build();


            IMessageRouteResolver csRouteResolver = configs.getChargingStationRouteResolver(csId);

            ChargingStationNatsIoClient chargingStationNatsIoClient = new ChargingStationNatsIoClient(csService, csRouteResolver, natsOptions);
            chargingStationNatsIoClient.connect();

            return chargingStationNatsIoClient;

        }
    }
}
