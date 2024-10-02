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
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallResultSerializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.RegistrationStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.*;

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

    @Test
    void unit_serialize_CallResultMessage_of_type_BootNotificationResponse_systemClockNow() {
        // Step 1: Generate a fixed 'now' clock.
        String dateString = "2024-07-17T12:54:48.106352+02:00";
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);
        Instant instant = offsetDateTime.toInstant();
        Clock fixedClock = Clock.fixed(instant, ZoneId.of("Europe/Copenhagen"));
        ZonedDateTime now = ZonedDateTime.now(fixedClock);

        // Step 2: Construct the response.
        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(now)
                .withInterval((int) Duration.ofMinutes(5).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResult =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId("19223201")
                        .withPayLoad(bootNotificationResponse)
                        .build();

        // Step 3: Test that the response was parsed as expected.
        try {
            String expected = "[3,\"19223201\",{\"currentTime\":\"2024-07-17T12:54:48+02:00\",\"interval\":300,\"status\":\"Accepted\"}]";

            String actual = CallResultSerializer.serialize(callResult);

            assertEquals(expected, actual);
        } catch (JsonProcessingException cause) {
            fail("Failed to deserialize.", cause);
        }
    }
}
