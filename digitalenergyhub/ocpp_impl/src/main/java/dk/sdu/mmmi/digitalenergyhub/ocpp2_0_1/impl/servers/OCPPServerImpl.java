package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.IOCPPServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.OCPPRequestHandler;
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
            logger.warning(String.format("Request handler for OCPPMessageType=%s already exists. Aborting.",
                    requestType.getValue()));
            return;
        }

        logger.warning(String.format("Added request handler for OCPPMessageType=%s.", requestType.getValue()));
        requestHandler.register(natsConnection);
        this.requestHandlers.put(requestType, requestHandler);
    }

    @Override
    public void removeRequestHandler(OCPPMessageType requestType) {
        if (!requestHandlers.containsKey(requestType)) {
            logger.warning(String.format("Request handler for OCPPMessageType=%s does not exists. Aborting.",
                    requestType.getValue()));
        }

        requestHandlers.remove(requestType);
    }

    @Override
    public boolean existsRequestHandler(OCPPMessageType requestType) {
        return requestHandlers.containsKey(requestType);
    }

    public IMessageRouteResolver getMsgRouteResolver() {
        return msgRouteResolver;
    }
}
