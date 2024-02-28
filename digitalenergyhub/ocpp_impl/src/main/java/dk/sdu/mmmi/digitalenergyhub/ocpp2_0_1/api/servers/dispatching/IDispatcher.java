package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.dispatching;

/**
 * This interface describes how to register/unregister a dispatcher.
 * @param <C> The type of the connection that is used to register the dispatcher.
 * @param <D> The type of the dispatcher.
 */
public interface IDispatcher<C, D> {
    D register(C connection);

    void unregister();

    D getDispatcher();
}
