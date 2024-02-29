package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.NatsRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;

/**
 * This interface represents a server that can manage dispatchers.
 */
public interface IOCPPServer {

    <IN extends ICallMessage<?>, OUT extends ICallResultMessage<?>> void addRequestHandler(OCPPMessageType msgType, NatsRequestHandler<IN, OUT> requestHandler);

    IMessageRouteResolver getMsgRouteResolver();
}
