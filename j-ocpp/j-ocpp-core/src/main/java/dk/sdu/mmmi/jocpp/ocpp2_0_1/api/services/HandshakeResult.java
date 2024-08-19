package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum HandshakeResult {
    ACCEPTED("Accepted"),
    REJECTED("Rejected");
    private final String value;
    private final static Map<String, HandshakeResult> CONSTANTS = new HashMap<>();

    static {
        for (HandshakeResult c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    HandshakeResult(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static HandshakeResult fromValue(String value) {
        HandshakeResult constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
