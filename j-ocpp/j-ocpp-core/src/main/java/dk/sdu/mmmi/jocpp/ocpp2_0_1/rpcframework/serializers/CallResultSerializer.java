package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;

import java.io.IOException;

/**
 * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
 * @param <T>
 */
public class CallResultSerializer<T> extends StdSerializer<ICallResult<T>> {


    protected CallResultSerializer(Class<ICallResult<T>> t) {
        super(t);
    }

    @Override
    public void serialize(ICallResult<T> callResultMessage, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        if (callResultMessage instanceof ICallError) {
            ICallError callError = (ICallError)callResultMessage;
            jsonGenerator.writeStartArray(); // Start
            jsonGenerator.writeNumber(callError.getMessageTypeId().getValue());
            jsonGenerator.writeString(callError.getMessageId());
            jsonGenerator.writeString(callError.getErrorCode());
            jsonGenerator.writeString(callError.getErrorDescription());

            if (!isValid(callError.getErrorDetails())) {
                String errMsg = "The errorDetails field [4] must be of type JSON object.";
                throw new JsonParseException(errMsg);
            }

            jsonGenerator.writeRaw("," + callError.getErrorDetails());
            jsonGenerator.writeEndArray(); // End
        } else { // Type is ICallResultMessage
            jsonGenerator.writeStartArray(); // Start
            jsonGenerator.writeNumber(callResultMessage.getMessageTypeId().getValue());
            jsonGenerator.writeString(callResultMessage.getMessageId());
            jsonGenerator.writeObject(callResultMessage.getPayload());
            jsonGenerator.writeEndArray(); // End
        }
    }

    /**
     * NB! Only works for complex data types. Does not work for primitives like String, Integer, etc.
     * @param callResult
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public static <T> String serialize(ICallResult<T> callResult) throws JsonProcessingException {
        try {
            String moduleName = String.format("%s<%s>",
                    CallResultSerializer.class.getName(),
                    callResult.getClass().getName());
            SimpleModule module = new SimpleModule(moduleName);
            module.addSerializer(new CallResultSerializer<>((Class<ICallResult<T>>) callResult.getClass()));

            ObjectMapper mapper = JacksonUtil.getDefault();
            mapper.registerModule(module);

            return mapper.writeValueAsString(callResult);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    private boolean isValid(String json) {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        try {
            mapper.readTree(json);
        } catch (JacksonException e) {
            return false;
        }

        return true;
    }
}