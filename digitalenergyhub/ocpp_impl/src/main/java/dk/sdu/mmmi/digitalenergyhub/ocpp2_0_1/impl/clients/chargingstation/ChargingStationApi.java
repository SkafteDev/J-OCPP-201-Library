package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.ICsmsProxy;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IOCPPServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfig;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerConnectorConfigs;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation.OCPPServerImpl;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class ChargingStationApi {

    private final ICsmsProxy csmsProxy;
    private final IOCPPServer csServer;
    private final Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationApi.class.getName());

    public ChargingStationApi(ICsmsProxy clientApi,
                              IOCPPServer server,
                              Connection natsConnection) {
        this.csmsProxy = clientApi;
        this.csServer = server;
        this.natsConnection = natsConnection;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    public ICsmsProxy getCsmsProxy() {
        return this.csmsProxy;
    }

    public IOCPPServer getChargingStationServer() {
        return this.csServer;
    }

    public static ChargingStationApiBuilder newBuilder() {
        return new ChargingStationApiBuilder();
    }

    public static class ChargingStationApiBuilder {
        private String csId;
        private IBrokerConnectorConfigs configs;

        public ChargingStationApiBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationApiBuilder withBrokerConnectorConfigs(IBrokerConnectorConfigs configs) {
            this.configs = configs;
            return this;
        }

        public ChargingStationApi build() {
            if (csId == null) throw new IllegalArgumentException("Charging Station ID must not be null. Provide a Charging Station Id.");
            if (configs == null) throw new IllegalArgumentException("BrokerConnectorConfigs must not be null. Provide a BrokerConnectorConfigs");

            BrokerConnectorConfig csBrokerConnectorConfig = configs.getConfigFromCsId(csId);

            try {
                Options natsOptions = Options.builder()
                        .server(csBrokerConnectorConfig.getBrokerUrl())
                        .connectionName(String.format("Charging Station Server operatorId=%s csmsId=%s csId=%s",
                                csBrokerConnectorConfig.getOperatorId(), csBrokerConnectorConfig.getCsmsId(), csId))
                        .connectionTimeout(Duration.ofMinutes(2))
                        .connectionListener((connection, eventType) -> {
                            logger.info(String.format("NATS.io connection event: %s%n", eventType));
                        })
                        .build();

                Connection natsClientConnection = Nats.connect(natsOptions);

                IMessageRouteResolver csRouteResolver = configs.getChargingStationRouteResolver(csId);

                IOCPPServer server = new OCPPServerImpl(natsClientConnection, csRouteResolver);
                ICsmsProxy clientApi = new CsmsProxyImpl(natsClientConnection, csRouteResolver);

                return new ChargingStationApi(clientApi, server, natsClientConnection);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
