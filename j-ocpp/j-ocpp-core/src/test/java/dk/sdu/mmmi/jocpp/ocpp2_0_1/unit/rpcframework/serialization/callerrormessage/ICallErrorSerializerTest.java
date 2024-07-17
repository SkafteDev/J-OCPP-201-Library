package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callerrormessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallResultSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ICallErrorSerializerTest {

    /**
     * Tests if a basic OCPP 2.0.1 CALLERROR (ICallError) can be serialized to a valid format.
     */
    @Test
    void unit_serialize_CallErrorMessage_NOT_IMPLEMENTED_empty_error_details() {
        ICallError callError = CallErrorImpl.newCallErrorBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription("SetDisplayMessageRequest not implemented.")
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        try {
            String expected = "[4,\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\"NotImplemented\",\"SetDisplayMessageRequest not implemented.\",{}]";

            String actual = CallResultSerializer.serialize(callError);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }

    /**
     * Tests if an invalid formatted OCPP 2.0.1 CALLERROR (ICallError) throws an exception on serialization.
     */
    @Test
    void unit_serialize_CallErrorMessage_NOT_IMPLEMENTED_invalid_error_details() {
        ICallError callError = CallErrorImpl.newCallErrorBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription("SetDisplayMessageRequest not implemented.")
                .withErrorDetails("invalid JSON object") // Must be of type JSON object i.e. "{}"
                .build();

        assertThrows(JsonProcessingException.class,
                () -> CallResultSerializer.serialize(callError));
    }
}
