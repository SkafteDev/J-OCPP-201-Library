package dk.sdu.mmmi.digitalenergyhub;

import com.google.protobuf.Timestamp;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IClient;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IPublisher;
import dk.sdu.mmmi.digitalenergyhub.nats.NatsClient;
import dk.sdu.mmmi.digitalenergyhub.protobuff.JSONMessage;
import dk.sdu.mmmi.digitalenergyhub.protobuff.Meta;
import dk.sdu.mmmi.digitalenergyhub.subscribers.JsonMessageSubscriber;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.util.UUID;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class Main {
    private static final String SUBJECT = "my_topic";

    public static void main(String[] args) {
        IClient<Message> natsClient = new NatsClient("nats://localhost:4222");
        startConsumer(natsClient);

        //startProducer();
    }

    private static void startProducer() {
        IPublisher<Message> natsPublisher = new NatsClient("nats://localhost:4222");

        new Thread(() -> {
            while (true) {
                JSONMessage protoPayload = getJSONMessage("Christian", "{ msg: \"hello\" }");

                Message natsMessage = NatsMessage.builder()
                        .data(protoPayload.toByteArray())
                        .subject(SUBJECT)
                        .build();
                natsPublisher.publish(SUBJECT, natsMessage);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void startConsumer(IClient<Message> natsClient) {
        natsClient.addSubscriber(SUBJECT, new JsonMessageSubscriber());

        new Thread(natsClient::startConsume).start();
    }

    private static JSONMessage getJSONMessage(String senderId, String payload) {
        Timestamp timestamp = fromMillis(System.currentTimeMillis());
        JSONMessage message = JSONMessage.newBuilder()
                .setMeta(
                        Meta.newBuilder()
                                .setMessageId(UUID.randomUUID().toString())
                                .setSenderId(senderId)
                                .setTimestamp(timestamp)
                                .build()
                )
                .setJsonPayload(payload)
                .build();
        return message;
    }
}
