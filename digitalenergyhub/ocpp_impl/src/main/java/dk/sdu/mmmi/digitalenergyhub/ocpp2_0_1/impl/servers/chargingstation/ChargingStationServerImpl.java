package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.NatsRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.SetChargingProfileHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfileStatusEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileResponse;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ChargingStationServerImpl extends AbstractChargingStationServer {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ChargingStationServerImpl(ChargingStationDeviceModel csDeviceModel, Connection natsConnection) {
        super(csDeviceModel, natsConnection);
    }

    @Override
    protected void addCancelReservationDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addCertificateSignedDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addChangeAvailabilityDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addClearCacheDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addClearChargingProfileDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addClearDisplayMessageDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addClearVariableMonitoringDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addCostUpdatedDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addCustomerInformationDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addDeleteCertificateDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetBaseReportDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetChargingProfilesDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetCompositeScheduleDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetDisplayMessagesDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetInstalledCertificateIdsDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetLocalListVersionDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetLogDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetMonitoringReportDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetReportDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetTransactionStatusDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addGetVariablesDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addInstallCertificateDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addMeterValuesDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addRequestStartTransactionDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addRequestStopTransactionDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addReserveNowDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addResetDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSendLocalListDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetChargingProfileDispatcher(Connection natsConnection) {
        var handler = new SetChargingProfileHandler(super.routingMap);
        handler.registerDispatcher(natsConnection);
    }

    @Override
    protected void addSetDisplayMessageDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetMonitoringBaseDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetMonitoringLevelDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetNetworkProfileDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetVariableMonitoringDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addSetVariablesDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addTriggerMessageDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addUnlockConnectorDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addUnpublishFirmwareDispatcher(Connection natsConnection) {

    }

    @Override
    protected void addUpdateFirmwareDispatcher(Connection natsConnection) {

    }
}
