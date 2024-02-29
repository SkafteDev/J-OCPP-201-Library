package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.dispatching.IDispatcher;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChargingStationServerImpl implements IChargingStationServer<Connection, Dispatcher> {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final IMessageRouteResolver msgRouteResolver;

    protected Connection natsConnection;

    protected Map<OCPPMessageType, IDispatcher<Connection, Dispatcher>> dispatchers;

    public ChargingStationServerImpl(Connection natsConnection, IMessageRouteResolver msgRouteResolver) {
        this.natsConnection = natsConnection;
        this.msgRouteResolver = msgRouteResolver;
        this.dispatchers = new HashMap<>();
    }

    @Override
    public void addDispatcher(OCPPMessageType requestType, IDispatcher<Connection, Dispatcher> dispatcher) {
        if (dispatchers.containsKey(requestType)) {
            logger.warning(String.format("Dispatcher for OCPPMessageType=%s already exists. Aborting.", requestType.getValue()));
            return;
        }

        logger.warning(String.format("Added dispatcher for OCPPMessageType=%s.", requestType.getValue()));
        dispatcher.register(natsConnection);
        this.dispatchers.put(requestType, dispatcher);
    }

    public IMessageRouteResolver getMsgRouteResolver() {
        return msgRouteResolver;
    }
}
