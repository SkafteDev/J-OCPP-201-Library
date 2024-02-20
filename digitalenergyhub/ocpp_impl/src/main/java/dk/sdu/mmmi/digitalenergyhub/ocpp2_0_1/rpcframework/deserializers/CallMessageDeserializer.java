package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.RFC3339DateFormat;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;

import java.io.IOException;

public class CallMessageDeserializer<T> extends StdDeserializer<ICallMessage<T>> {
    public CallMessageDeserializer(Class<?> vc) {
        super(vc);
    }

    public CallMessageDeserializer() {
        this(null);
    }

    @Override
    public ICallMessage<T> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        MessageType messageTypeId = MessageType.valueOf((Integer) node.get(0).numberValue());
        String messageId = node.get(1).textValue();
        String action = node.get(2).textValue();
        String payload = node.get(3).toString();

        Class<T> payloadType = (Class<T>) _valueClass;

        T payloadObj = new ObjectMapper().readValue(payload, payloadType);

        ICallMessage<T> result = CallMessageImpl.<T>newBuilder()
                .withMessageId(messageId)
                .asAction(action)
                .withPayLoad(payloadObj)
                .build();

        return result;
    }

    public static <T> ICallMessage<T> deserialize(String jsonInput, Class<T> payloadType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ICallMessage.class, new CallMessageDeserializer<T>(payloadType));
        mapper.registerModule(module);
        mapper.setDateFormat(RFC3339DateFormat.getDateFormat());

        ICallMessage<T> callMessage = mapper.readValue(jsonInput, ICallMessage.class);

        return callMessage;
    }
}
