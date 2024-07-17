package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JacksonUtil {
    private JacksonUtil() {
    }

    public static ObjectMapper getDefault() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(ZonedDateTime.class, new CustomZonedDateTimeSerializer());

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(javaTimeModule) // Needed to automatically register the com.fasterxml.jackson.datatype:jackson-datatype-jsr310 module.
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        return mapper;
    }

    /**
     * Custom formatter to ensure RFC3339 compatibility.
     * E.g.:
     * '2024-07-17T12:54:48+02:00'  for UTC+2
     * '2024-07-17T10:54:48Z'       for UTC+0
     */
    private static class CustomZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        protected CustomZonedDateTimeSerializer() {
            super(ZonedDateTime.class);
        }

        @Override
        public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(zonedDateTime.format(formatter));
        }
    }
}
