package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallMessageSerializerTest {
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

        ICallMessage<BootNotificationRequest> callMessage = CallMessageImpl.<BootNotificationRequest>newBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .asAction("BootNotification")
                .withPayLoad(bootNotificationRequest)
                .build();

        try {
            String expected = "[2,\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\"BootNotification\"," +
                    "{\"chargingStation\":{\"model\":\"SingleSocketCharger\",\"vendorName\":\"VendorX\"},\"reason\":\"PowerUp\"}]";

            String actual = CallMessageSerializer.serialize(callMessage);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }
}