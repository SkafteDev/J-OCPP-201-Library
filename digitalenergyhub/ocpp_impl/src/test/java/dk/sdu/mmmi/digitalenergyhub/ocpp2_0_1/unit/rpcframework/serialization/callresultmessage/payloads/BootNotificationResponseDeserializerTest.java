package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.unit.rpcframework.serialization.callresultmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BootNotificationResponseDeserializerTest {

    @Test
    void unit_deserialize_CallResultMessage_of_type_BootNotificationResponse() {
        String jsonInput = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32Z\",\"interval\":300,\"status\":\"Accepted\"}]";

        try {
            ICallResultMessage<BootNotificationResponse> deserialized = CallResultMessageDeserializer.deserialize(jsonInput, BootNotificationResponse.class);

            // Test that we can reconstruct the original json string from the deserialized object.
            String serialized = CallResultMessageSerializer.serialize(deserialized);

            assertEquals(jsonInput, serialized);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
