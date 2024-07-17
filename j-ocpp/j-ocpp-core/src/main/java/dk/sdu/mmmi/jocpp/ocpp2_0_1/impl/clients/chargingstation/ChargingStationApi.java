package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.chargingstation.ICsmsProxy;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.OCPPRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerConfig;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class ChargingStationApi {

    private final ICsmsProxy csmsProxy;
    private final IRequestHandlerRegistry csServer;
    private final Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationApi.class.getName());

    public ChargingStationApi(ICsmsProxy proxy,
                              IRequestHandlerRegistry server,
                              Connection natsConnection) {
        this.csmsProxy = proxy;
        this.csServer = server;
        this.natsConnection = natsConnection;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    public ICsmsProxy getCsmsProxy() {
        return this.csmsProxy;
    }

    public IRequestHandlerRegistry getChargingStationServer() {
        return this.csServer;
    }

    public static ChargingStationApiBuilder newBuilder() {
        return new ChargingStationApiBuilder();
    }

    public static class ChargingStationApiBuilder {
        private String csId;
        private IBrokerContext configs;

        public ChargingStationApiBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationApiBuilder withBrokerContext(IBrokerContext configs) {
            this.configs = configs;
            return this;
        }

        public ChargingStationApi build() {
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

                IRequestHandlerRegistry server = new OCPPRequestHandlerRegistry(natsClientConnection, csRouteResolver);
                ICsmsProxy clientApi = new CsmsProxyImpl(natsClientConnection, csRouteResolver);

                return new ChargingStationApi(clientApi, server, natsClientConnection);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
