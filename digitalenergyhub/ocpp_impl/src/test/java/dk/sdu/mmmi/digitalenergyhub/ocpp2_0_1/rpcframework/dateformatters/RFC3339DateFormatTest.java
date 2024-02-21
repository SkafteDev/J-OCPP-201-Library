package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.dateformatters;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.RFC3339DateFormat;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class RFC3339DateFormatTest {

    @Test
    void unit_getDateFormatUTC() {
        LocalDateTime localDateTime = LocalDateTime.of(2019, 4, 12, 23, 20, 50);
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.UTC);

        Date d = new Date(epochSecond * 1000);

        SimpleDateFormat rfc3339 = RFC3339DateFormat.getDateFormat();

        String actual = rfc3339.format(d);
        String expected = "2019-04-12T23:20:50Z";

        assertEquals(expected, actual);
    }

    @Test
    void unit_getDateFormatUTC_plus_1() {
        LocalDateTime localDateTime = LocalDateTime.of(2019, 4, 12, 23, 20, 50);
        long epochSecond = localDateTime.toEpochSecond(ZoneOffset.of("+02:00"));

        Date d = new Date(epochSecond * 1000);

        SimpleDateFormat rfc3339 = RFC3339DateFormat.getDateFormat(TimeZone.getTimeZone("Africa/Cairo")); // Africa/Cairo is in GMT+2.

        String actual = rfc3339.format(d);
        String expected = "2019-04-12T23:20:50+02:00";

        assertEquals(expected, actual);
    }
}