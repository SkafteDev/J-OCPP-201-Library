package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallError;
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