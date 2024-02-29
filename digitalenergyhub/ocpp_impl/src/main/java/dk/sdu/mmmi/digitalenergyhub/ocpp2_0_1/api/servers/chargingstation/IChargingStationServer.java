package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.dispatching.IDispatcher;

/**
 * This interface represents a server that can manage dispatchers.
 * @param <C> The type of the connection that is used to register the dispatcher.
 * @param <D> The type of the dispatcher.
 */
public interface IChargingStationServer<C, D>  {

    void addDispatcher(OCPPMessageType msgType, IDispatcher<C, D> dispatcher);

    IMessageRouteResolver getMsgRouteResolver();
}
