
package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;

import java.io.IOException;

/**
 * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
 * @param <T>
 */
public class CallSerializer<T> extends StdSerializer<ICall<T>> {


    protected CallSerializer(Class<ICall<T>> t) {
        super(t);
    }

    @Override
    public void serialize(ICall<T> callMessage, JsonGenerator jsonGenerator,
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
    public static <T> String serialize(ICall<T> callMessage) throws JsonProcessingException {
        try {
            String moduleName = String.format("%s<%s>",
                    CallSerializer.class.getName(),
                    callMessage.getClass().getName());
            SimpleModule module = new SimpleModule(moduleName);
            module.addSerializer(new CallSerializer<>((Class<ICall<T>>) callMessage.getClass()));

            ObjectMapper mapper = JacksonUtil.getDefault();
            mapper.registerModule(module);

            return mapper.writeValueAsString(callMessage);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
}
