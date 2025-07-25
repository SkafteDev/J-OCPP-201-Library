
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
