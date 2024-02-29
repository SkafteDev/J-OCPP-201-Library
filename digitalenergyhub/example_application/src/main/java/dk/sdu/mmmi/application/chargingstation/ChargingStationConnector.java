package dk.sdu.mmmi.application.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationClientNatsIo;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRouteResolverImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation.ChargingStationServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class ChargingStationConnector {

    private final IChargingStationClientApi csApi;
    private final IChargingStationServer<Connection, Dispatcher> csServer;
    private final Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationConnector.class.getName());

    public ChargingStationConnector(IChargingStationClientApi clientApi,
                                    IChargingStationServer<Connection, Dispatcher> server,
                                    Connection natsConnection) {
        this.csApi = clientApi;
        this.csServer = server;
        this.natsConnection = natsConnection;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    public IChargingStationClientApi getChargingStationApi() {
        return this.csApi;
    }

    public IChargingStationServer<Connection, Dispatcher> getChargingStationServer() {
        return this.csServer;
    }

    public static ChargingStationConnectorBuilder newBuilder() {
        return new ChargingStationConnectorBuilder();
    }

    public static class ChargingStationConnectorBuilder {
        private String operatorId;
        private String csmsId;
        private String csId;
        private String natsConnectionUrl;
        private IMessageRouteResolver resolver;

        public ChargingStationConnectorBuilder withOperatorId(String operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public ChargingStationConnectorBuilder withCsmsId(String csmsId) {
            this.csmsId = csmsId;
            return this;
        }

        public ChargingStationConnectorBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationConnectorBuilder withMessageRouteResolver(IMessageRouteResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        public ChargingStationConnectorBuilder withNatsConnectionUrl(String natsConnectionUrl) {
            this.natsConnectionUrl = natsConnectionUrl;
            return this;
        }

        public ChargingStationConnector build() {
            try {
                Options natsOptions = Options.builder()
                        .server(natsConnectionUrl)
                        .connectionName(String.format("Charging Station Server operatorId=%s csmsId=%s csId=%s",
                                operatorId, csmsId, csId))
                        .connectionTimeout(Duration.ofMinutes(2))
                        .connectionListener((connection, eventType) -> {
                            logger.info(String.format("NATS.io connection event: %s%n", eventType));
                        })
                        .build();

                Connection natsClientConnection = Nats.connect(natsOptions);

                IChargingStationServer server = new ChargingStationServerImpl(natsClientConnection, null);
                IMessageRouteResolver routingMap = new MessageRouteResolverImpl(operatorId, csmsId, csId);
                IChargingStationClientApi clientApi = new ChargingStationClientNatsIo(natsClientConnection, routingMap);

                return new ChargingStationConnector(clientApi, server, natsClientConnection);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
