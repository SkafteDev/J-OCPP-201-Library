package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.devicemodel.ChargingStationDeviceModel;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.dispatching.IDispatcher;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChargingStationServerImpl implements IChargingStationServer<Connection, Dispatcher> {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final IMessageRoutingMap routingMap;

    protected ChargingStationDeviceModel chargingStationDeviceModel;

    protected Connection natsConnection;

    protected Map<OCPPMessageType, IDispatcher<Connection, Dispatcher>> dispatchers;

    public ChargingStationServerImpl(ChargingStationDeviceModel csDeviceModel,
                                     Connection natsConnection) {
        this.natsConnection = natsConnection;
        this.routingMap = new MessageRoutingMapImpl(csDeviceModel.getOperatorId(),
                csDeviceModel.getCsmsId(),
                csDeviceModel.getCsId());
        this.chargingStationDeviceModel = csDeviceModel;
        this.dispatchers = new HashMap<>();
    }

    @Override
    public void addDispatcher(OCPPMessageType requestType, IDispatcher<Connection, Dispatcher> dispatcher) {
        if (dispatchers.containsKey(requestType)) return;

        dispatcher.register(natsConnection);
        this.dispatchers.put(requestType, dispatcher);
    }

    public IMessageRoutingMap getRoutingMap() {
        return routingMap;
    }
}
