package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api;

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
}
