/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callerrormessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers.CallResultDeserializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ICallErrorDeserializerTest {

    /**
     * Tests if a basic OCPP 2.0.1 CALLERROR (ICallError) can be deserialized from valid JSON.
     */
    @Test
    void unit_serialize_CallErrorMessage_NOT_IMPLEMENTED_empty_error_details() {
        try {
            ICallError expected = CallErrorImpl.newCallErrorBuilder()
                    .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                    .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                    .withErrorDescription("SetDisplayMessageRequest not implemented.")
                    .withErrorDetails("{}") // Must be of type JSON object.
                    .build();

            String json = "[4,\n" +
                    "\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\n" +
                    "\"NotImplemented\",\n" +
                    "\"SetDisplayMessageRequest not implemented.\",\n" +
                    "{}\n" +
                    "]";

            ICallError actual = (ICallError) CallResultDeserializer.deserialize(json, Object.class);

            assertEquals(expected.getMessageId(), actual.getMessageId());
            assertEquals(expected.getErrorCode(), actual.getErrorCode());
            assertEquals(expected.getErrorDescription(), actual.getErrorDescription());
            assertEquals(expected.getErrorDetails(), actual.getErrorDetails());
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }

    /**
     * Tests if a basic OCPP 2.0.1 CALLERROR (ICallError) throws an exception given invalid JSON.
     */
    @Test
    void unit_serialize_CallErrorMessage_invalid_error_details() {

            String json = "[4," +
                    "\"82854779-f4d2-49c8-9c3c-437ce47e523c\"," +
                    "\"NotImplemented\"," +
                    "\"SetDisplayMessageRequest not implemented.\"," +
                    "\"invalid object\"" + "]";

            assertThrows(JsonProcessingException.class,
                    () -> CallResultDeserializer.deserialize(json, Object.class));
    }
}
