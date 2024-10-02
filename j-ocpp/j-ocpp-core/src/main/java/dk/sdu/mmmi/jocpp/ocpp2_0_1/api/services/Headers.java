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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import java.util.*;

/**
 * Represents headers that can be included with an OCPP (Open Charge Point Protocol) message.
 *
 * <p><strong>Note:</strong> These headers are not defined by the OCPP specification.</p>
 */
public class Headers {

    private final Map<String, Collection<String>> headers = new HashMap<>();

    public Headers() {}

    public static Headers emptyHeader() {
        return new Headers();
    }

    public void put(String header, String value) {
        if (!headers.containsKey(header)) {
            headers.put(header, new HashSet<>());
        }

        headers.get(header).add(value);
    }

    public Collection<String> get(String header) {
        if (headers.containsKey(header)) {
            return headers.get(header);
        } else {
            return new HashSet<>();
        }
    }

    public String getFirst(String header) {
        Collection<String> values = this.headers.get(header);

        return values == null ? null :
                values.stream().findFirst().orElse(null);
    }

    public void put(String header, Collection<String> values) {
        for (String val : values) {
            put(header, val);
        }
    }

    public void putAll(Map<String, Collection<String>> headers) {
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            String header = entry.getKey();
            Collection<String> values = entry.getValue();
            put(header, values);
        }
    }

    public boolean contains(String header) {
        return headers.containsKey(header);
    }

    public Map<String, Collection<String>> getAll() {
        return new HashMap<>(headers);
    }

    public enum HeaderEnum {

        // Header that can be included in the request from CS -> CSMS to identify the sending Charging Station.
        CS_ID("ChargingStation-Identity");

        private final String value;

        HeaderEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
