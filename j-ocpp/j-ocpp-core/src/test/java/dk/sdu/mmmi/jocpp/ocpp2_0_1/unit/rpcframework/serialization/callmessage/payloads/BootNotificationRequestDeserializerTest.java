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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers.CallDeserializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BootNotificationRequestDeserializerTest {

    @Test
    void unit_deserialize_serialize_roundtrip_CallMessage_of_type_BootNotificationRequest() {
        String jsonInput = "[2,\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\"BootNotification\"," +
                "{\"chargingStation\":{\"model\":\"SingleSocketCharger\",\"vendorName\":\"VendorX\"},\"reason\":\"PowerUp\"}]";

        try {
            ICall<BootNotificationRequest> deserialized = CallDeserializer.deserialize(jsonInput, BootNotificationRequest.class);
            String serialized = CallSerializer.serialize(deserialized);

            assertEquals(jsonInput, serialized);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
