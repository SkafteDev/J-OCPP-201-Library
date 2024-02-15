package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import com.fasterxml.jackson.databind.node.IntNode;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;


import java.io.IOException;

public class CallMessageDeserializer<T> extends StdDeserializer<ICallMessage<T>> {

    protected CallMessageDeserializer(Class<?> vc) {
        super(vc);
    }

    public static <T> ICallMessage<T> deserialize(String json, Class<T> payloadType) throws JsonProcessingException {
        /*
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(payloadType,new CallMessageDeserializer<T>((Class<ICallMessage<T>>) type.getClass()))


            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);

            return mapper.writeValueAsString(callMessage);
        } catch (JsonProcessingException e) {
            throw e;
        }
        */

        return null;
    }

    @Override
    public ICallMessage<T> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {

        JsonNode node = jp.getCodec().readTree(jp);

        MessageType messageTypeId = MessageType.values()[(Integer) node.get(0).numberValue()];
        String messageId = node.get(1).textValue();
        String action = node.get(2).textValue();
        String payload = node.get(3).textValue();

        T payloadObj = new ObjectMapper().readValue(payload, T.class);

        ICallMessage<T> result = CallMessageImpl.<T>newBuilder()
                .withMessageId(messageId)
                .asAction(action)
                .withPayLoad(payloadObj)
                .build();

        return result;
    }
}
