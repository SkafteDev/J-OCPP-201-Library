package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.ChargingStation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BootNotificationRequestSerializerTest {
    @Test
    void unit_serialize_CallMessage_of_type_BootNotificationRequest() {
        BootNotificationRequest bootNotificationRequest = new BootNotificationRequest.BootNotificationRequestBuilder()
                .withReason(BootReasonEnum.POWER_UP)
                .withChargingStation(
                        new ChargingStation.ChargingStationBuilder()
                                .withModel("SingleSocketCharger")
                                .withVendorName("VendorX")
                                .build())
                .build();

        ICall<BootNotificationRequest> callMessage = CallImpl.<BootNotificationRequest>newBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .asAction("BootNotification")
                .withPayLoad(bootNotificationRequest)
                .build();

        try {
            String expected = "[2,\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\"BootNotification\"," +
                    "{\"chargingStation\":{\"model\":\"SingleSocketCharger\",\"vendorName\":\"VendorX\"},\"reason\":\"PowerUp\"}]";

            String actual = CallSerializer.serialize(callMessage);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }
}