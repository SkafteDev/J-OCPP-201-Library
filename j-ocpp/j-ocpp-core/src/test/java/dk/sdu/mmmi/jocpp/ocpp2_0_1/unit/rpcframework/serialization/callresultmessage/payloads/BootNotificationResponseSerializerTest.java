package dk.sdu.mmmi.jocpp.ocpp2_0_1.unit.rpcframework.serialization.callresultmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallResultSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.RegistrationStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BootNotificationResponseSerializerTest {
    @Test
    void unit_serialize_CallResultMessage_of_type_BootNotificationResponse() {
        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(ZonedDateTime.of(2013, 2, 1, 20, 53, 32, 0, ZoneId.of("UTC")))
                .withInterval((int) Duration.ofMinutes(5).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResultMessage =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId("19223201")
                        .withPayLoad(bootNotificationResponse)
                        .build();

        try {
            String expected = "[3,\"19223201\",{\"currentTime\":\"2013-02-01T20:53:32Z\",\"interval\":300,\"status\":\"Accepted\"}]";

            String actual = CallResultSerializer.serialize(callResultMessage);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }
}
