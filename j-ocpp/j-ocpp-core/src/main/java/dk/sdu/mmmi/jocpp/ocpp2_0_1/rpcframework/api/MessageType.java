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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api;

public enum MessageType {

    CALL(2),
    CALLRESULT(3),
    CALLERROR(4);

    private final int value;
    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageType valueOf(int value) {
        for (MessageType enumValue : values()) {
            if (enumValue.getValue() == value) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException("No enum for 'MessageType' with the with value " + value);
    }
}
