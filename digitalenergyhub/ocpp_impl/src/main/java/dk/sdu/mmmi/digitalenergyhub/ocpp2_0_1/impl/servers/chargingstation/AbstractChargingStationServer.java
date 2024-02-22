package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public abstract class AbstractChargingStationServer implements IChargingStationServer {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    protected final String natsConnectionUrl;
    protected final IMessageRoutingMap routingMap;

    protected ChargingStationDeviceModel chargingStationDeviceModel;

    protected Connection natsConnection;

    public AbstractChargingStationServer(ChargingStationDeviceModel csDeviceModel,
                                         String natsConnectionUrl) {
        this.natsConnectionUrl = natsConnectionUrl;
        this.routingMap = new MessageRoutingMapImpl(csDeviceModel.getOperatorId(),
                csDeviceModel.getCsmsId(),
                csDeviceModel.getCsId());
        this.chargingStationDeviceModel = csDeviceModel;
    }

    @Override
    public void connect() {
        Options natsOptions = Options.builder()
                .server(natsConnectionUrl)
                .connectionName(String.format("Charging Station Server operatorId=%s csmsId=%s csId=%s",
                        chargingStationDeviceModel.getOperatorId(), chargingStationDeviceModel.getCsmsId(),
                        chargingStationDeviceModel.getCsId()))
                .connectionTimeout(Duration.ofMinutes(2))
                .connectionListener((connection, eventType) -> {
                    logger.info(String.format("NATS.io connection event: %s%n", eventType));
                })
                .build();

        try {
            natsConnection = Nats.connect(natsOptions);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void serve() {
        // Add dispatchers for all incoming message types
        addCancelReservationDispatcher(natsConnection);
        addCertificateSignedDispatcher(natsConnection);
        addChangeAvailabilityDispatcher(natsConnection);
        addClearCacheDispatcher(natsConnection);
        addClearChargingProfileDispatcher(natsConnection);
        addClearDisplayMessageDispatcher(natsConnection);
        addClearVariableMonitoringDispatcher(natsConnection);
        addCostUpdatedDispatcher(natsConnection);
        addCustomerInformationDispatcher(natsConnection);
        addDeleteCertificateDispatcher(natsConnection);
        addGetBaseReportDispatcher(natsConnection);
        addGetChargingProfilesDispatcher(natsConnection);
        addGetCompositeScheduleDispatcher(natsConnection);
        addGetDisplayMessagesDispatcher(natsConnection);
        addGetInstalledCertificateIdsDispatcher(natsConnection);
        addGetLocalListVersionDispatcher(natsConnection);
        addGetLogDispatcher(natsConnection);
        addGetMonitoringReportDispatcher(natsConnection);
        addGetReportDispatcher(natsConnection);
        addGetTransactionStatusDispatcher(natsConnection);
        addGetVariablesDispatcher(natsConnection);
        addInstallCertificateDispatcher(natsConnection);
        addMeterValuesDispatcher(natsConnection);
        addRequestStartTransactionDispatcher(natsConnection);
        addRequestStopTransactionDispatcher(natsConnection);
        addReserveNowDispatcher(natsConnection);
        addResetDispatcher(natsConnection);
        addSendLocalListDispatcher(natsConnection);
        addSetChargingProfileDispatcher(natsConnection);
        addSetDisplayMessageDispatcher(natsConnection);
        addSetMonitoringBaseDispatcher(natsConnection);
        addSetMonitoringLevelDispatcher(natsConnection);
        addSetNetworkProfileDispatcher(natsConnection);
        addSetVariableMonitoringDispatcher(natsConnection);
        addSetVariablesDispatcher(natsConnection);
        addTriggerMessageDispatcher(natsConnection);
        addUnlockConnectorDispatcher(natsConnection);
        addUnpublishFirmwareDispatcher(natsConnection);
        addUpdateFirmwareDispatcher(natsConnection);
    }
    protected abstract void addCancelReservationDispatcher(Connection natsConnection);
    protected abstract void addCertificateSignedDispatcher(Connection natsConnection);
    protected abstract void addChangeAvailabilityDispatcher(Connection natsConnection);
    protected abstract void addClearCacheDispatcher(Connection natsConnection);
    protected abstract void addClearChargingProfileDispatcher(Connection natsConnection);
    protected abstract void addClearDisplayMessageDispatcher(Connection natsConnection);
    protected abstract void addClearVariableMonitoringDispatcher(Connection natsConnection);
    protected abstract void addCostUpdatedDispatcher(Connection natsConnection);
    protected abstract void addCustomerInformationDispatcher(Connection natsConnection);
    protected abstract void addDeleteCertificateDispatcher(Connection natsConnection);
    protected abstract void addGetBaseReportDispatcher(Connection natsConnection);
    protected abstract void addGetChargingProfilesDispatcher(Connection natsConnection);
    protected abstract void addGetCompositeScheduleDispatcher(Connection natsConnection);
    protected abstract void addGetDisplayMessagesDispatcher(Connection natsConnection);
    protected abstract void addGetInstalledCertificateIdsDispatcher(Connection natsConnection);
    protected abstract void addGetLocalListVersionDispatcher(Connection natsConnection);
    protected abstract void addGetLogDispatcher(Connection natsConnection);
    protected abstract void addGetMonitoringReportDispatcher(Connection natsConnection);
    protected abstract void addGetReportDispatcher(Connection natsConnection);
    protected abstract void addGetTransactionStatusDispatcher(Connection natsConnection);
    protected abstract void addGetVariablesDispatcher(Connection natsConnection);
    protected abstract void addInstallCertificateDispatcher(Connection natsConnection);
    protected abstract void addMeterValuesDispatcher(Connection natsConnection);
    protected abstract void addRequestStartTransactionDispatcher(Connection natsConnection);
    protected abstract void addRequestStopTransactionDispatcher(Connection natsConnection);
    protected abstract void addReserveNowDispatcher(Connection natsConnection);
    protected abstract void addResetDispatcher(Connection natsConnection);
    protected abstract void addSendLocalListDispatcher(Connection natsConnection);
    protected abstract void addSetChargingProfileDispatcher(Connection natsConnection);
    protected abstract void addSetDisplayMessageDispatcher(Connection natsConnection);
    protected abstract void addSetMonitoringBaseDispatcher(Connection natsConnection);
    protected abstract void addSetMonitoringLevelDispatcher(Connection natsConnection);
    protected abstract void addSetNetworkProfileDispatcher(Connection natsConnection);
    protected abstract void addSetVariableMonitoringDispatcher(Connection natsConnection);
    protected abstract void addSetVariablesDispatcher(Connection natsConnection);
    protected abstract void addTriggerMessageDispatcher(Connection natsConnection);
    protected abstract void addUnlockConnectorDispatcher(Connection natsConnection);
    protected abstract void addUnpublishFirmwareDispatcher(Connection natsConnection);
    protected abstract void addUpdateFirmwareDispatcher(Connection natsConnection);
}
