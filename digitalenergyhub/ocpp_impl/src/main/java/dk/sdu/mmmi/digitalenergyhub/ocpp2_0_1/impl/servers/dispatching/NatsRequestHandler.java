package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public abstract class NatsRequestHandler<IN extends ICallMessage<?>, OUT extends ICallResultMessage<?>> implements IDispatcher<Connection> {
    private final Logger logger = Logger.getLogger(NatsRequestHandler.class.getName());
    private final Class<?> inboundPayloadType;
    private final Class<?> outboundPayloadType;

    public NatsRequestHandler(Class<?> inPayloadType, Class<?> outPayloadType) {
        this.inboundPayloadType = inPayloadType;
        this.outboundPayloadType = outPayloadType;
    }

    @Override
    public void registerDispatcher(Connection natsConnection) {
        Dispatcher dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received request payload='%s' on subject '%s'",
                    jsonPayload,
                    natsMsg.getSubject()));

            IN callMessage = deserialize(jsonPayload);

            // Handle the message internally, e.g. update state, store in db, etc.
            handleInternally(callMessage);

            OUT callResult = getResponse(callMessage);

            String jsonResponsePayload = serialize(callResult);

            Message natsResponse = NatsMessage.builder()
                    .subject(natsMsg.getReplyTo()) // Reply subject must use the call's provided reply subject.
                    .data(jsonResponsePayload)
                    .build();

            natsConnection.publish(natsResponse);

        });
        dispatcher.subscribe(getInboundMessageRoute());
    }

    public IN deserialize(String rawJsonPayload) {
        ICallMessage<?> deserialized;

        try {
            deserialized = CallMessageDeserializer.deserialize(rawJsonPayload, inboundPayloadType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return (IN) deserialized;
    }
    public abstract void handleInternally(IN message);
    public abstract OUT getResponse(IN message);

    public String serialize(OUT message) {
        try {
            return CallResultMessageSerializer.serialize((ICallResultMessage<?>) message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public abstract String getInboundMessageRoute();
}
