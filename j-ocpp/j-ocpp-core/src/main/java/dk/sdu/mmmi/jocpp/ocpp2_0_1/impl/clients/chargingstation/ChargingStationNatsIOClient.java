package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.ICSClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPOverNatsIOService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ChargingStationNatsIOClient implements ICSClient {
    private final IMessageRouteResolver routeResolver;
    private ICsmsServiceEndpoint csmsProxy;
    private IRequestHandlerRegistry csService;
    private Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationNatsIOClient.class.getName());
    private final Options natsOptions;

    public ChargingStationNatsIOClient(IMessageRouteResolver routeResolver,
                                       Options natsOptions) {
        this.routeResolver = routeResolver;
        this.natsOptions = natsOptions;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    @Override
    public void connect() {
        try {
            this.natsConnection = Nats.connect(this.natsOptions);
            this.csService = new OCPPOverNatsIOService(routeResolver);

            // TODO: Send handshake to external server
            ICsmsService csmsServiceProxy = new ICsmsService() {
                @Override
                public ICsmsServiceEndpoint connect(HandshakeRequest handshakeRequest) {
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

                        HandshakeResponse handshakeResponse = mapper.readValue(respJsonPayload, HandshakeResponseImpl.class);
                        logger.info(String.format("Handshake response: %s", respJsonPayload));

                        return new CsmsProxyNatsIO(natsConnection, routeResolver);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            this.csmsProxy = csmsServiceProxy.connect(HandshakeRequestImpl.HandshakeInfoImplBuilder.newBuilder()
                    .withIdentity(routeResolver.getCsIdentity())
                    .withOcppVersion(HandshakeOcppVersion.OCPP_201)
                    .build());

        } catch (IOException | InterruptedException e ) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ICsmsServiceEndpoint getCsmsEndpoint() {
        return this.csmsProxy;
    }

    public IRequestHandlerRegistry getCsEndpoint() {
        return this.csService;
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

    public static ChargingStationNatsIOClientBuilder newBuilder() {
        return new ChargingStationNatsIOClientBuilder();
    }

    public static class ChargingStationNatsIOClientBuilder {
        private String csId;
        private IBrokerContext configs;

        public ChargingStationNatsIOClientBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationNatsIOClientBuilder withBrokerContext(IBrokerContext configs) {
            this.configs = configs;
            return this;
        }

        public ChargingStationNatsIOClient build() {
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

            return new ChargingStationNatsIOClient(csRouteResolver, natsOptions);

        }
    }
}
