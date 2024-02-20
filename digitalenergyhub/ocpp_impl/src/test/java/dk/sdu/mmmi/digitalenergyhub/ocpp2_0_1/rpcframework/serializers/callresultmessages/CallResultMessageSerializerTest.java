package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.callresultmessages;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.RegistrationStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CallResultMessageSerializerTest {

    //@Test
    void unit_serialize_CallResultMessage_of_type_String() {
        ICallResultMessage<String> callResultMessage = CallResultMessageImpl.<String>newBuilder()
                .withMessageId("19223201")
                .withPayLoad("{\"currentTime\":\"2013-02-01T20:53:32.486Z\",\"interval\":300,\"status\":\"Accepted\"}")
                .build();

        try {
            String expected = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32.486Z\",\"interval\":300,\"status\":\"Accepted\"}]";

            String actual = CallResultMessageSerializer.serialize(callResultMessage);
            System.out.println(actual);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }

    @Test
    void unit_serialize_CallResultMessage_of_type_BootNotificationResponse() {
        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(DateUtil.of(2013, 2, 1, 20, 53, 32))
                .withInterval((int) Duration.ofMinutes(5).toSeconds())
                .build();

        ICallResultMessage<BootNotificationResponse> callResultMessage =
                CallResultMessageImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId("19223201")
                        .withPayLoad(bootNotificationResponse)
                        .build();

        try {
            String expected = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32Z\",\"interval\":300,\"status\":\"Accepted\"}]";

            String actual = CallResultMessageSerializer.serialize(callResultMessage);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }
}
