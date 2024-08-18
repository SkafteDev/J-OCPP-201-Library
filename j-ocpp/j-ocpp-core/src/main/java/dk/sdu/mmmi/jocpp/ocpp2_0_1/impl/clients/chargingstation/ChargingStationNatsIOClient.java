package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPOverNatsIOService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerConfig;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class ChargingStationNatsIOClient {
    private final IMessageRouteResolver routeResolver;
    private final ICsmsServiceEndpoint csmsProxy;
    private final IRequestHandlerRegistry csService;
    private final Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationNatsIOClient.class.getName());

    public ChargingStationNatsIOClient(IMessageRouteResolver routeResolver,
                                       ICsmsServiceEndpoint proxy,
                                       IRequestHandlerRegistry requestHandlerRegistry,
                                       Connection natsConnection) {
        this.routeResolver = routeResolver;
        this.csmsProxy = proxy;
        this.csService = requestHandlerRegistry;
        this.natsConnection = natsConnection;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    public ICsmsServiceEndpoint getCsmsProxy() {
        return this.csmsProxy;
    }

    public IRequestHandlerRegistry getService() {
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

            try {
                Options natsOptions = Options.builder()
                        .server(csBrokerConfig.getBrokerUrl())
                        .connectionName(String.format("Charging Station Server operatorId=%s csmsId=%s csId=%s",
                                csBrokerConfig.getOperatorId(), csBrokerConfig.getCsmsId(), csId))
                        .connectionTimeout(Duration.ofMinutes(2))
                        .connectionListener((connection, eventType) -> {
                            logger.info(String.format("NATS.io connection event: %s%n", eventType));
                        })
                        .build();

                Connection natsClientConnection = Nats.connect(natsOptions);

                IMessageRouteResolver csRouteResolver = configs.getChargingStationRouteResolver(csId);

                IRequestHandlerRegistry csService = new OCPPOverNatsIOService(natsClientConnection, csRouteResolver);
                ICsmsServiceEndpoint csmsProxy = new CsmsProxyNatsIO(natsClientConnection, csRouteResolver);

                return new ChargingStationNatsIOClient(csRouteResolver, csmsProxy, csService, natsClientConnection);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
