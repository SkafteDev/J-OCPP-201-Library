package dk.sdu.mmmi.application.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationClientNatsIo;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation.ChargingStationServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class ChargingStationFacade {

    private final IChargingStationClientApi csApi;
    private final IChargingStationServer csServer;
    private final ChargingStationDeviceModel csDeviceModel;
    private final Connection natsConnection;

    private static final Logger logger = Logger.getLogger(ChargingStationFacade.class.getName());

    public ChargingStationFacade(IChargingStationClientApi clientApi,
                                 IChargingStationServer server,
                                 Connection natsConnection,
                                 ChargingStationDeviceModel csDeviceModel) {
        this.csApi = clientApi;
        this.csServer = server;
        this.natsConnection = natsConnection;
        this.csDeviceModel = csDeviceModel;
    }

    public ChargingStationDeviceModel getCsDeviceModel() {
        return csDeviceModel;
    }

    public Connection getNatsConnection() {
        return natsConnection;
    }

    public IChargingStationClientApi getChargingStationApi() {
        return this.csApi;
    }

    public IChargingStationServer getChargingStationServer() {
        return this.csServer;
    }

    public static ChargingStationFacadeBuilder newBuilder() {
        return new ChargingStationFacadeBuilder();
    }

    public static class ChargingStationFacadeBuilder {

        private String operatorId;
        private String csmsId;
        private String csId;
        private String vendorName;
        private String model;
        private String firmwareVersion;
        private String serialNumber;
        private String natsConnectionUrl;

        public ChargingStationFacadeBuilder withOperatorId(String operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public ChargingStationFacadeBuilder withCsmsId(String csmsId) {
            this.csmsId = csmsId;
            return this;
        }

        public ChargingStationFacadeBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public ChargingStationFacadeBuilder withVendorName(String vendorName) {
            this.vendorName = vendorName;
            return this;
        }

        public ChargingStationFacadeBuilder withModel(String model) {
            this.model = model;
            return this;
        }

        public ChargingStationFacadeBuilder withFirmwareVersion(String firmwareVersion) {
            this.firmwareVersion = firmwareVersion;
            return this;
        }

        public ChargingStationFacadeBuilder withSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public ChargingStationFacadeBuilder withNatsConnectionUrl(String natsConnectionUrl) {
            this.natsConnectionUrl = natsConnectionUrl;
            return this;
        }

        public ChargingStationFacade build() {
            ChargingStationDeviceModel csDeviceModel = new ChargingStationDeviceModel(csId, operatorId, csmsId,
                    ChargingStation.builder()
                            .withVendorName(vendorName)
                            .withModel(model)
                            .withFirmwareVersion(firmwareVersion)
                            .withSerialNumber(serialNumber)
                            .build());

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

                IChargingStationServer server = new ChargingStationServerImpl(csDeviceModel, natsClientConnection);
                IMessageRoutingMap routingMap = new MessageRoutingMapImpl(operatorId, csmsId, csId);
                IChargingStationClientApi clientApi = new ChargingStationClientNatsIo(natsClientConnection, routingMap);

                return new ChargingStationFacade(clientApi, server, natsClientConnection, csDeviceModel);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
