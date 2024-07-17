package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.util;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonUtil {
    private JacksonUtil() {
    }

    public static ObjectMapper getDefault() {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()) // Needed to automatically register the com.fasterxml.jackson.datatype:jackson-datatype-jsr310 module.
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        return mapper;
    }
}
