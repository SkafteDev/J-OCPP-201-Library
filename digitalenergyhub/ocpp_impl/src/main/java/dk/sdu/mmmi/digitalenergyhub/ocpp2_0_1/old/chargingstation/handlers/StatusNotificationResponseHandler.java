package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.chargingstation.handlers;

import com.google.protobuf.InvalidProtocolBufferException;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.messagetypes.OCPPMessageToSubjectMapping;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.StatusNotificationResponse;
import io.nats.client.Message;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class StatusNotificationResponseHandler implements IMessageHandler<Message> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final OCPPMessageToSubjectMapping.OCPPMessageType ocppMessageType =
            OCPPMessageToSubjectMapping.OCPPMessageType.StatusNotificationResponse;

    public OCPPMessageToSubjectMapping.OCPPMessageType getOcppMessageType() {
        return ocppMessageType;
    }

    @Override
    public void onMessageReceived(String subject, Message msg, Object connection) {
        try {
            StatusNotificationResponse statusNotificationResponse = StatusNotificationResponse.parseFrom(msg.getData());
            LocalDateTime ldt = LocalDateTime.now();

            String logMsg = String.format("%s received message type %s on subject %s:%n%s",
                    ldt,
                    ocppMessageType,
                    subject,
                    statusNotificationResponse);
            logger.info(logMsg);

            // TODO: Handle the message and act accordingly to the OCPP 2.0.1 protocol.

        } catch (InvalidProtocolBufferException e) {
            logger.warning(e.getMessage());
        }
    }
}
