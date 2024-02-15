package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import org.junit.jupiter.api.Test;

public class CallMessageDeserializerTest {

    @Test
    void unit_deserialize_CallMessage_of_type_BootNotificationRequest() {
        String jsonInput = "[2,\"82854779-f4d2-49c8-9c3c-437ce47e523c\",\"BootNotification\"," +
                "{\"chargingStation\":{\"model\":\"SingleSocketCharger\",\"vendorName\":\"VendorX\"},\"reason\":\"PowerUp\"}]";

        TypeReference<ICallMessage<BootNotificationRequest>> typeReference = new
                TypeReference<ICallMessage<BootNotificationRequest>>(){};

        ObjectMapper mapper = new ObjectMapper();

        try {
            ICallMessage<BootNotificationRequest> bootNotificationRequest = mapper.readValue(jsonInput, typeReference);

            System.out.println(bootNotificationRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
