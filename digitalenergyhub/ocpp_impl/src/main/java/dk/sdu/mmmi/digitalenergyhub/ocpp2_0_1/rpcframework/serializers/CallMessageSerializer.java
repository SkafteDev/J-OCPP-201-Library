package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;

import java.io.IOException;

/**
 * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
 * @param <T>
 */
public class CallMessageSerializer<T> extends StdSerializer<ICallMessage<T>> {


    protected CallMessageSerializer(Class<ICallMessage<T>> t) {
        super(t);
    }

    @Override
    public void serialize(ICallMessage<T> callMessage, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();

        jsonGenerator.writeNumber(callMessage.getMessageTypeId().getValue());
        jsonGenerator.writeString(callMessage.getMessageId());
        jsonGenerator.writeString(callMessage.getAction());
        jsonGenerator.writeObject(callMessage.getPayload());

        jsonGenerator.writeEndArray();
    }

    /**
     * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
     * @param callMessage
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public static <T> String serialize(ICallMessage<T> callMessage) throws JsonProcessingException {
        try {
            SimpleModule module = new SimpleModule();
            module.addSerializer(new CallMessageSerializer<>((Class<ICallMessage<T>>) callMessage.getClass()));

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);
            mapper.setDateFormat(RFC3339DateFormat.getDateFormat());

            return mapper.writeValueAsString(callMessage);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}
