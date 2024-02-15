package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;


import java.io.IOException;

public class CallMessageDeserializer<T> extends StdDeserializer<ICallMessage<T>> {

    protected CallMessageDeserializer(Class<ICallMessage<T>> t) {
        super(t);
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
    public ICallMessage<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
