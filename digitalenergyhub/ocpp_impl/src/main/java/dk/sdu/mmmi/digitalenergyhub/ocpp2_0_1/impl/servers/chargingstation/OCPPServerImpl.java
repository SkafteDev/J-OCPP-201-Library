package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IOCPPServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.OCPPRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileResponse;
import io.nats.client.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OCPPServerImpl implements IOCPPServer {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final IMessageRouteResolver msgRouteResolver;

    protected Connection natsConnection;

    protected Map<OCPPMessageType, OCPPRequestHandler<?, ?>> requestHandlers;

    public OCPPServerImpl(Connection natsConnection, IMessageRouteResolver msgRouteResolver) {
        this.natsConnection = natsConnection;
        this.msgRouteResolver = msgRouteResolver;
        this.requestHandlers = new HashMap<>();
    }

    @Override
    public <IN, OUT> void addRequestHandler(OCPPMessageType requestType, OCPPRequestHandler<IN, OUT> requestHandler) {
        if (requestHandlers.containsKey(requestType)) {
            logger.warning(String.format("Dispatcher for OCPPMessageType=%s already exists. Aborting.", requestType.getValue()));
            return;
        }

        logger.warning(String.format("Added dispatcher for OCPPMessageType=%s.", requestType.getValue()));
        requestHandler.register(natsConnection);
        this.requestHandlers.put(requestType, requestHandler);
    }

    public IMessageRouteResolver getMsgRouteResolver() {
        return msgRouteResolver;
    }
}
