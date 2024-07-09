package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {

    private DateUtil() {}

    /**
     * Returns the UTC time encapsulated as a Date object.
     * @return
     */
    public static Date now() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        long epochMilliseconds = localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000;

        return new Date(epochMilliseconds);
    }

    /**
     * Returns the UTC time encapsulated as a Date object.
     * @return
     */
    public static Date of(int year, int month, int day, int hour, int minute, int second) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        long epochMilliseconds = localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000;

        return new Date(epochMilliseconds);
    }
}
