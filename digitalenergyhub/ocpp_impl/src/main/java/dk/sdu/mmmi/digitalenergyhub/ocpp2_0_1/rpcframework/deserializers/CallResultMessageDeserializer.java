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
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.RFC3339DateFormat;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;

import java.io.IOException;

public class CallResultMessageDeserializer<T> extends StdDeserializer<ICallResultMessage<T>> {
    public CallResultMessageDeserializer(Class<?> vc) {
        super(vc);
    }

    public CallResultMessageDeserializer() {
        this(null);
    }

    @Override
    public ICallResultMessage<T> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        MessageType messageTypeId = MessageType.valueOf((Integer) node.get(0).numberValue());
        String messageId = node.get(1).textValue();
        String payload = node.get(2).toString();

        Class<T> payloadType = (Class<T>) _valueClass;

        T payloadObj = new ObjectMapper().readValue(payload, payloadType);

        ICallResultMessage<T> result = CallResultMessageImpl.<T>newBuilder()
                .withMessageId(messageId)
                .withPayLoad(payloadObj)
                .build();

        return result;
    }


    public static <T> ICallResultMessage<T> deserialize(String jsonInput, Class<T> payloadType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ICallResultMessage.class, new CallResultMessageDeserializer<T>(payloadType));
        mapper.registerModule(module);
        mapper.setDateFormat(RFC3339DateFormat.getDateFormat());

        ICallResultMessage<T> callResultMessage = mapper.readValue(jsonInput, ICallResultMessage.class);

        return callResultMessage;
    }
}