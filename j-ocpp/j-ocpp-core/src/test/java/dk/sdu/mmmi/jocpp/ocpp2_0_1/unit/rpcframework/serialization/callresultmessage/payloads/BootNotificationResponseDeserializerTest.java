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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callresultmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers.CallResultDeserializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallResultSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BootNotificationResponseDeserializerTest {

    @Test
    void unit_deserialize_CallResultMessage_of_type_BootNotificationResponse() {
        String jsonInput = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32Z\",\"interval\":300,\"status\":\"Accepted\"}]";

        try {
            ICallResult<BootNotificationResponse> deserialized = CallResultDeserializer.deserialize(jsonInput, BootNotificationResponse.class);

            // Test that we can reconstruct the original json string from the deserialized object.
            String serialized = CallResultSerializer.serialize(deserialized);

            assertEquals(jsonInput, serialized);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
