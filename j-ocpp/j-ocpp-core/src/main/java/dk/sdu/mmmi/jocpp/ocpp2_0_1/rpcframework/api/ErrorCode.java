package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api;

public enum ErrorCode {

    /**
     * Payload for Action is syntactically incorrect.
     */
    FORMAT_VIOLATION("FormatViolation"),

    /**
     * Any other error not covered by the more specific error codes in this table.
     */
    GENERIC_ERROR("GenericError"),

    /**
     * A message with an Message Type Number received that is not supported by this
     * implementation.
     */
    MESSAGE_TYPE_NOT_SUPPORTED("MessageTypeNotSupported"),

    /**
     * Requested Action is not known by receiver.
     */
    NOT_IMPLEMENTED("NotImplemented"),

    /**
     * Requested Action is recognized but not supported by the receiver.
     */
    NOT_SUPPORTED("NotSupported"),

    /**
     * Payload for Action is syntactically correct but at least one of the fields violates occurrence
     * constraints.
     */
    OCCURRENCE_CONSTRAINT_VIOLATION("OccurrenceConstraintViolation"),

    /**
     * Payload is syntactically correct but at least one field contains an invalid value.
     */
    PROPERTY_CONSTRAINT_VIOLATION("PropertyConstraintViolation"),

    /**
     * Payload for Action is not conform the PDU structure.
     */
    PROTOCOL_ERROR("ProtocolError"),

    /**
     * Content of the call is not a valid RPC Request, for example: MessageId could not be read.
     */
    RPC_FRAMEWORK_ERROR("RpcFrameworkError"),

    /**
     * During the processing of Action a security issue occurred preventing receiver from
     * completing the Action successfully.
     */
    SECURITY_ERROR("SecurityError"),

    /**
     * Payload for Action is syntactically correct but at least one of the fields violates data type
     * constraints (e.g. "somestring": 12).
     */
    TYPE_CONSTRAINT_VIOLATION("TypeConstraintViolation");

    private final String value;
    ErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ErrorCode fromString(String value) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (value.equalsIgnoreCase(errorCode.getValue())) {
                return errorCode;
            }
        }

        throw new IllegalArgumentException("Enum contains no constant matching " + value);
    }
}
