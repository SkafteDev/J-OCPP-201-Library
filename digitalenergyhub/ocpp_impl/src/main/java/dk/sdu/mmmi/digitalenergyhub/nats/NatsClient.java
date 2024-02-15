package dk.sdu.mmmi.digitalenergyhub.nats;

import dk.sdu.mmmi.digitalenergyhub.interfaces.IRequester;
import dk.sdu.mmmi.digitalenergyhub.interfaces.ISubscriber;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IPublisher;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import io.nats.client.*;
import io.nats.client.impl.NatsMessage;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class NatsClient implements ISubscriber<Message>, IPublisher<Message>, IRequester<Message, Connection> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private Connection natsConnection;

    private final Map<String, List<IMessageHandler<Message>>> messageHandlers; // Key = subject, Value = list of handlers for that subject.

    private final Map<String, Subscription> natsSubscriptions; // Key = subject, value = list of nats subscriptions.

    public NatsClient(String connectionString) {
        connectToBroker(connectionString);
        messageHandlers = new HashMap<>();
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
        var subscribers = messageHandlers.get(subject);

        for (var subscriber : subscribers) {
            subscriber.onMessageReceived(subject, message, this.natsConnection);
        }
    }

    @Override
    public void addSubscriber(String subject, IMessageHandler<Message> s) {
        if (!messageHandlers.containsKey(subject)) {
            messageHandlers.put(subject, new LinkedList<>());
        }

        if (!natsSubscriptions.containsKey(subject)) {
            Dispatcher dispatcher = natsConnection.createDispatcher();
            Subscription subscription = dispatcher.subscribe(subject, message -> {
                s.onMessageReceived(subject, message, natsConnection);
            });
            natsSubscriptions.put(subject, subscription);
        }

        messageHandlers.get(subject).add(s);
    }

    @Override
    public void removeSubscriber(String subject, IMessageHandler<Message> s) {
        if (!messageHandlers.containsKey(subject)) {
            return;
        }

        messageHandlers.get(subject).remove(s);
    }

    @Override
    public boolean publish(String subject, Message msg) {
        try {
            NatsMessage overridenMsg = NatsMessage.builder()
                    .subject(subject)
                    .replyTo(msg.getReplyTo())
                    .headers(msg.getHeaders())
                    .data(msg.getData())
                    .build();
            natsConnection.publish(overridenMsg);
            natsConnection.flush(Duration.ZERO);

        } catch (TimeoutException | InterruptedException e) {
            logger.warning(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Message request(Message request, Duration timeout, Connection conn) {
        try {
            return conn.request(request, timeout);
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
            return null;
        }
    }
}
