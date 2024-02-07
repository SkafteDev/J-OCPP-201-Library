package dk.sdu.mmmi.digitalenergyhub.subscribers;

import com.google.protobuf.InvalidProtocolBufferException;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.protobuff.JSONMessage;
import io.nats.client.Message;

public class JsonMessageMessageHandler implements IMessageHandler<Message> {
    @Override
    public void onMessageReceived(String subject, Message msg, Object connection) {
        try {
            byte[] rawData = msg.getData();
            JSONMessage jsonMessage = JSONMessage.parseFrom(rawData);
            System.out.printf("Received msg on subject %s%n", subject);
            System.out.printf("Raw: %s%n", jsonMessage);
        } catch (InvalidProtocolBufferException e) {

        }
    }
}
