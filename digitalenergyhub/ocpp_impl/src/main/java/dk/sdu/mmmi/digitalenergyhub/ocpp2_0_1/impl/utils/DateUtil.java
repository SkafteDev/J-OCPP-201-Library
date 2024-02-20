package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {

    private DateUtil() {}

    /**
     * Returns a new Date object with a default ZoneOffset = UTC.
     * @return
     */
    public static Date now() {
        return now(ZoneOffset.UTC);
    }

    /**
     * Returns a new Date object with the given ZoneOffset.
     * @return
     */
    public static Date now(ZoneOffset offSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        long epochMilliseconds = localDateTime.toEpochSecond(offSet) * 1000;

        return new Date(epochMilliseconds);
    }

    /**
     * Creates a new Date object with a default ZoneOffset = UTC.
     * @return
     */
    public static Date of(int year, int month, int day, int hour, int minute, int second) {
        return of(year, month, day, hour, minute, second, ZoneOffset.UTC);
    }

    /**
     * Creates a new Date object with the given ZoneOffset.
     * @return
     */
    public static Date of(int year, int month, int day, int hour, int minute, int second, ZoneOffset offSet) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        long epochMilliseconds = localDateTime.toEpochSecond(offSet) * 1000;

        return new Date(epochMilliseconds);
    }
}
