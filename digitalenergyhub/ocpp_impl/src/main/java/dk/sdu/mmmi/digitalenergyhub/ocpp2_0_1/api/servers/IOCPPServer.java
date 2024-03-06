package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;

/**
 * This interface represents a server that can manage request handlers.
 */
public interface IOCPPServer {

    <IN, OUT> void addRequestHandler(OCPPMessageType msgType, OCPPRequestHandler<IN, OUT> requestHandler);

    void removeRequestHandler(OCPPMessageType msgType);

    boolean existsRequestHandler(OCPPMessageType msgType);

    IMessageRouteResolver getMsgRouteResolver();
}
