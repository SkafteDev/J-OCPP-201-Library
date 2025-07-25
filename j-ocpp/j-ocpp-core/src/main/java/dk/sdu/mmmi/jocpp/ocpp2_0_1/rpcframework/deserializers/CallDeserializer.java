
package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util.JacksonUtil;

import java.io.IOException;

public class CallDeserializer<T> extends StdDeserializer<ICall<T>> {
    public CallDeserializer(Class<?> vc) {
        super(vc);
    }

    public CallDeserializer() {
        this(null);
    }

    @Override
    public ICall<T> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        MessageType messageTypeId = MessageType.valueOf((Integer) node.get(0).numberValue());
        String messageId = node.get(1).textValue();
        String action = node.get(2).textValue();
        String payload = node.get(3).toString();

        Class<T> payloadType = (Class<T>) _valueClass;

        T payloadObj = JacksonUtil.getDefault().readValue(payload, payloadType);

        ICall<T> result = CallImpl.<T>newBuilder()
                .withMessageId(messageId)
                .asAction(action)
                .withPayLoad(payloadObj)
                .build();

        return result;
    }

    public static <T> ICall<T> deserialize(String jsonInput, Class<T> payloadType) throws JsonProcessingException {
        String moduleName = String.format("%s<%s>",
                CallDeserializer.class.getName(),
                payloadType.getName());
        SimpleModule module = new SimpleModule(moduleName);
        module.addDeserializer(ICall.class, new CallDeserializer<T>(payloadType));

        ObjectMapper mapper = JacksonUtil.getDefault();
        mapper.registerModule(module);

        ICall<T> callMessage = mapper.readValue(jsonInput, ICall.class);

        return callMessage;
    }
}
