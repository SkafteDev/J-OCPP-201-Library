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

public abstract class OCPPRequestHandler<IN, OUT> {
    private final Logger logger = Logger.getLogger(OCPPRequestHandler.class.getName());
    private final Class<IN> inboundPayloadType;
    private final Class<OUT> outboundPayloadType;

    private Dispatcher dispatcher;

    public OCPPRequestHandler(Class<IN> inPayloadType, Class<OUT> outPayloadType) {
        this.inboundPayloadType = inPayloadType;
        this.outboundPayloadType = outPayloadType;
    }

    public Dispatcher register(Connection natsConnection) {
        if (dispatcher != null) return dispatcher;

        this.dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received request payload='%s' on subject '%s'",
                    jsonPayload,
                    natsMsg.getSubject()));

            ICallMessage<IN> callMessage = deserialize(jsonPayload);

            // Handle the message internally, e.g. update state, store in db, etc.
            handle(callMessage);

            ICallResultMessage<OUT> callResult = generateResponse(callMessage);

            String jsonResponsePayload = serialize(callResult);

            Message natsResponse = NatsMessage.builder()
                    .subject(natsMsg.getReplyTo()) // Reply subject must use the call's provided reply subject.
                    .data(jsonResponsePayload)
                    .build();

            natsConnection.publish(natsResponse);

        });
        dispatcher.subscribe(getInboundMessageRoute());
        return dispatcher;
    }

    public void unregister() {
        dispatcher.unsubscribe(getInboundMessageRoute());
    }
    
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public ICallMessage<IN> deserialize(String rawJsonPayload) {
        ICallMessage<IN> deserialized;

        try {
            deserialized = CallMessageDeserializer.deserialize(rawJsonPayload, inboundPayloadType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return deserialized;
    }
    public abstract void handle(ICallMessage<IN> message);
    public abstract ICallResultMessage<OUT> generateResponse(ICallMessage<IN> message);

    public String serialize(ICallResultMessage<OUT> message) {
        try {
            return CallResultMessageSerializer.serialize(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public abstract String getInboundMessageRoute();
}
