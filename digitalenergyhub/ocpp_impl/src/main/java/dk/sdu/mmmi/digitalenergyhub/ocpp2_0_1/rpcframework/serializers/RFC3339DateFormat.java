package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class RFC3339DateFormat {

    /**
     * Instantiation of a static utility class not allowed.
     */
    private RFC3339DateFormat() {}

    /**
     * Returns the RFC3339 date format for the JVM default time zone.
     * @return
     */
    public static SimpleDateFormat getDateFormat() {
        return getDateFormat(TimeZone.getDefault());
    }

    /**
     * Returns the RFC3339 date format for the specified time zone.
     * @return
     */
    public static SimpleDateFormat getDateFormat(TimeZone tz) {
        if (tz == null) {
            throw new IllegalArgumentException("TimeZone must not null. Provide a TimeZone.");
        }

        // Create SimpleDateFormat with RFC3339 pattern
        SimpleDateFormat rfc3339Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        // Set time zone
        rfc3339Format.setTimeZone(tz);

        return rfc3339Format;
    }
}
