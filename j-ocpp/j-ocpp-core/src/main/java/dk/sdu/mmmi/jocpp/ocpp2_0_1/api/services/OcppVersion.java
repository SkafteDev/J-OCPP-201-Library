
package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/*
 * Valid values as defined in 'OCPP 2.0.1: Part 4 - JSON over WebSockets implementation guide':
 *
 * ocpp1.2
 * ocpp1.5
 * ocpp1.6
 * ocpp2.0
 * ocpp2.0.1
 */
public enum OcppVersion {
    OCPP_12("ocpp1.2"),
    OCPP_15("ocpp1.5"),
    OCPP_16("ocpp1.6"),
    OCPP_20("ocpp2.0"),
    OCPP_201("ocpp2.0.1");
    private final String value;
    private final static Map<String, OcppVersion> CONSTANTS = new HashMap<>();

    static {
        for (OcppVersion c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    OcppVersion(String value) {
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
    public static OcppVersion fromValue(String value) {
        OcppVersion constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }
}
