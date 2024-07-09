package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void ofUTC() {
        Date date = DateUtil.of(2024, 1, 1, 0, 0, 0);

        long expectedEpoch = 1704067200000L;
        long actualEpoch = date.getTime();

        String expectedString = "2024-01-01T00:00:00Z";
        String actualString = date.toInstant().toString();

        assertEquals(expectedEpoch, actualEpoch);
        assertEquals(expectedString, actualString);
    }
}