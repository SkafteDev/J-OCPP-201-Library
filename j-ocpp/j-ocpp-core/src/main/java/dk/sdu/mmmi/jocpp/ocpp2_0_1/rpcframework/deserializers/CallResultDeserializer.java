
package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;

import java.io.IOException;

public class CallResultDeserializer<T> extends StdDeserializer<ICallResult<T>> {
    public CallResultDeserializer(Class<?> vc) {
        super(vc);
    }

    public CallResultDeserializer() {
        this(null);
    }

    @Override
    public ICallResult<T> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        MessageType messageTypeId = MessageType.valueOf((Integer) node.get(0).numberValue());

        if (messageTypeId == MessageType.CALLRESULT) {
            String messageId = node.get(1).textValue();
            String payload = node.get(2).toString();

            Class<T> payloadType = (Class<T>) _valueClass;

            T payloadObj = JacksonUtil.getDefault().readValue(payload, payloadType);

            ICallResult<T> result = CallResultImpl.<T>newBuilder()
                    .withMessageId(messageId)
                    .withPayLoad(payloadObj)
                    .build();

            return result;
        } else if (messageTypeId == MessageType.CALLERROR){
            String messageId = node.get(1).textValue();
            ErrorCode errorCode = ErrorCode.fromString(node.get(2).textValue());
            String errorDescription = node.get(3).textValue();

            if (!node.get(4).isObject()) {
                String errMsg = "The errorDetails field [4] must be of type JSON object.";
                throw new JsonParseException(errMsg);
            }

            String errorDetails = node.get(4).toString();

            ICallError callError = CallErrorImpl.newCallErrorBuilder()
                    .withMessageId(messageId)
                    .withErrorCode(errorCode)
                    .withErrorDescription(errorDescription)
                    .withErrorDetails(errorDetails)
                    .build();
            return (ICallResult<T>) callError;
        } else {
            String errMsg = String.format("The message type '%s' is not valid for ICallResultMessage. Must be either " +
                    "'3' or '4'", messageTypeId);
            throw new JsonParseException(errMsg);
        }
    }

    public static <T> ICallResult<T> deserialize(String jsonInput, Class<T> payloadType) throws JsonProcessingException {
        String moduleName = String.format("%s<%s>",
                CallResultDeserializer.class.getName(),
                payloadType.getName());
        SimpleModule module = new SimpleModule(moduleName);
        module.addDeserializer(ICallResult.class, new CallResultDeserializer<T>(payloadType));

        ObjectMapper mapper = JacksonUtil.getDefault();
        mapper.registerModule(module);

        ICallResult<T> callResultMessage = mapper.readValue(jsonInput, ICallResult.class);

        return callResultMessage;
    }
}