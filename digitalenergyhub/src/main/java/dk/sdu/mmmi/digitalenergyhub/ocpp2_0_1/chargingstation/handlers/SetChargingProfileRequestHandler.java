package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.handlers;

import com.google.protobuf.InvalidProtocolBufferException;
import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes.OCPPMessageToSubjectMapping;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.IChargingStationServer;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.enumerations.ChargingProfileStatusEnumType;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.SetChargingProfileRequest;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.SetChargingProfileResponse;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes.OCPPMessageToSubjectMapping.OCPPMessageType;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class SetChargingProfileRequestHandler implements IMessageHandler<Message> {
    private final IChargingStationServer<Message> receiver;
    private Logger logger = Logger.getLogger(getClass().getName());

    private final OCPPMessageToSubjectMapping.OCPPMessageType ocppMessageType =
            OCPPMessageToSubjectMapping.OCPPMessageType.SetChargingProfileRequest;

    public OCPPMessageToSubjectMapping.OCPPMessageType getOcppMessageType() {
        return ocppMessageType;
    }

    public SetChargingProfileRequestHandler(IChargingStationServer<Message> receiver) {
        this.receiver = receiver;
    }

    @Override
    public void onMessageReceived(String subject, Message msg, Object connection) {
        try {
            SetChargingProfileRequest setChargingProfileRequest = SetChargingProfileRequest.parseFrom(msg.getData());
            LocalDateTime ldt = LocalDateTime.now();

            String logMsg = String.format("%s received message type %s on subject %s:%n%s",
                    ldt,
                    ocppMessageType,
                    subject,
                    setChargingProfileRequest);
            logger.info(logMsg);

            // TODO: Handle the message and act accordingly to the OCPP 2.0.1 protocol.
            SetChargingProfileResponse responsePayload = SetChargingProfileResponse.newBuilder()
                    .setStatus(ChargingProfileStatusEnumType.Accepted)
                    .build();

            Message responseNatsMsg = NatsMessage.builder()
                    .subject(msg.getReplyTo())
                    .data(responsePayload.toByteArray())
                    .build();

            receiver.emitResponse(OCPPMessageType.SetChargingProfileResponse, responseNatsMsg);

        } catch (InvalidProtocolBufferException e) {
            logger.warning(e.getMessage());
        }
    }
}
