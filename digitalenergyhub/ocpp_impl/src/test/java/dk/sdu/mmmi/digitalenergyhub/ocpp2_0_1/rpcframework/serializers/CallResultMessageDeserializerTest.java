package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallResultMessageDeserializerTest {

    @Test
    void unit_deserialize_serialize_roundtrip_CallResultMessage_of_type_String() {
        String jsonInput = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32.486Z\",\"interval\":300,\"status\":\"Accepted\"}]";


        try {
            ICallResultMessage<String> deserialized = CallResultMessageDeserializer.deserialize(jsonInput,
                    String.class);
            String serialized = CallResultMessageSerializer.serialize(deserialized);

            assertEquals(jsonInput, serialized);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
