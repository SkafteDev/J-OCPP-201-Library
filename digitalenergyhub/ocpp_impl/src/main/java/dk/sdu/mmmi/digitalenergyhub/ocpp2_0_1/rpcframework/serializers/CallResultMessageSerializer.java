package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;

import java.io.IOException;

public class CallResultMessageSerializer<T> extends StdSerializer<ICallResultMessage<T>> {


    protected CallResultMessageSerializer(Class<ICallResultMessage<T>> t) {
        super(t);
    }

    @Override
    public void serialize(ICallResultMessage<T> callResultMessage, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        jsonGenerator.writeNumber(callResultMessage.getMessageTypeId().getValue());
        jsonGenerator.writeString(callResultMessage.getMessageId());
        jsonGenerator.writeRaw(",");
        jsonGenerator.writeRaw(callResultMessage.getPayload().toString());

        jsonGenerator.writeEndArray();
    }

    public static <T> String serialize(ICallResultMessage<T> callResult) throws JsonProcessingException {
        try {
            SimpleModule module = new SimpleModule();
            module.addSerializer(new CallResultMessageSerializer<>((Class<ICallResultMessage<T>>) callResult.getClass()));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);

            return mapper.writeValueAsString(callResult);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}