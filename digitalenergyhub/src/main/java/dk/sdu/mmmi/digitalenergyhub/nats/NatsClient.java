package dk.sdu.mmmi.digitalenergyhub.nats;

import dk.sdu.mmmi.digitalenergyhub.interfaces.ISubscriber;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IPublisher;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class NatsSubscriber implements ISubscriber<Message>, IPublisher<Message> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private Connection natsConnection;

    private final Map<String, List<IMessageHandler<Message>>> internalSubscribers; // Key = subject, Value = list of subscribers.

    private final Map<String, Subscription> natsSubscriptions; // Key = subject, value = list of nats subscriptions.

    public NatsSubscriber(String connectionString) {
        connectToBroker(connectionString);
        internalSubscribers = new HashMap<>();
        natsSubscriptions = new HashMap<>();
    }

    private void connectToBroker(String connectionString) {
        try {
            natsConnection = Nats.connect(connectionString);
        } catch (IOException | InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void startConsume() {
        while (true) {
            natsSubscriptions.entrySet().stream().parallel().forEach(es -> {
                try {
                    Message msg = es.getValue().nextMessage(Duration.ZERO);
                    notifySubscribers(es.getKey(), msg);
                } catch (InterruptedException e) {
                    logger.warning(e.getMessage());
                }
            });
        }
    }

    private void notifySubscribers(String subject, Message message) {
        var subscribers = internalSubscribers.get(subject);

        for (var subscriber : subscribers) {
            subscriber.onMessageReceived(subject, message, this.natsConnection);
        }
    }

    @Override
    public void addSubscriber(String subject, IMessageHandler<Message> s) {
        if (!internalSubscribers.containsKey(subject)) {
            internalSubscribers.put(subject, new LinkedList<>());
        }

        if (!natsSubscriptions.containsKey(subject)) {
            Subscription subscription = natsConnection.subscribe(subject);
            natsSubscriptions.put(subject, subscription);
        }

        internalSubscribers.get(subject).add(s);
    }

    @Override
    public void removeSubscriber(String subject, IMessageHandler<Message> s) {
        if (!internalSubscribers.containsKey(subject)) {
            return;
        }

        internalSubscribers.get(subject).remove(s);
    }

    @Override
    public boolean publish(Message msg) {
        try {
            natsConnection.publish(msg);
            natsConnection.flush(Duration.ZERO);

        } catch (TimeoutException | InterruptedException e) {
            logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean publish(String subject, Message msg) {
        return publish(msg);
    }
}
