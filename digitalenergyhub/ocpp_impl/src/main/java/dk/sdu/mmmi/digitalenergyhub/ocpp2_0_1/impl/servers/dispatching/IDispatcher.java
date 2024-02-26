package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching;

/**
 * This interface represents a dispatcher that can receive CallMessages and handle them accordingly.
 * @param <C> The type of the connection that is used to register the dispatcher.
 */
public interface IDispatcher<C> {
    void registerDispatcher(C connection);
}
