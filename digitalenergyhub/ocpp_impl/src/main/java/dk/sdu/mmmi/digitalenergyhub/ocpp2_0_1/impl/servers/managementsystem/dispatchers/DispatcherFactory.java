package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.dispatchers;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;

/**
 * This class is responsible for creating NATS.io dispatchers for handling incoming messages.
 */
public class DispatcherFactory {
    public static Dispatcher createBootNotificationRequestDispatcher(String listenToSubject,
                                                                     Connection natsConnection) {

        Dispatcher dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            // TODO: Handle the BootNotificationRequest
            System.out.println(natsMsg);
        });
        dispatcher.subscribe(listenToSubject);

        return dispatcher;
    }
}
