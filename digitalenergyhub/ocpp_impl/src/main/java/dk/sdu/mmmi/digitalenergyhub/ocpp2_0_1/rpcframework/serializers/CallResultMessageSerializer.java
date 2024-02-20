package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;

import java.io.IOException;

/**
 * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
 * @param <T>
 */
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
        jsonGenerator.writeObject(callResultMessage.getPayload());

        jsonGenerator.writeEndArray();
    }

    /**
     * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
     * @param callResult
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public static <T> String serialize(ICallResultMessage<T> callResult) throws JsonProcessingException {
        try {
            SimpleModule module = new SimpleModule();
            module.addSerializer(new CallResultMessageSerializer<>((Class<ICallResultMessage<T>>) callResult.getClass()));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);
            mapper.setDateFormat(RFC3339DateFormat.getDateFormat());

            return mapper.writeValueAsString(callResult);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}