package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api;

/**
 * We only use CALLERROR in two situations:
 * 1. An error occurred during the transport of the message. This can be a network issue, an availability of service issue, etc.
 * 2. The call is received but the content of the call does not meet the requirements for a proper message. This could be missing
 * mandatory fields, an existing call with the same unique identifier is being handled already, unique identifier too long, invalid
 * JSON or OCPP syntax etc.
 * When a server needs to report a 'failure to process the message', the server SHALL use a Message Type: CallError
 * (MessageTypeNumber = 4).
 * When a server receives a corrupt message, the CALLERROR SHALL NOT directly include syntactically invalid JSON (For example,
 * without encoding it first). When also the MessageId cannot be read, the CALLERROR SHALL contain "-1" as MessageId.
 * When a message contains any invalid OCPP and/or it is not conform the JSON schema, the system is allowed to drop the message.
 * A CALLERROR always consists of 5 elements: The standard elements MessageTypeId and MessageId, an errorCode string, an
 * errorDescription string and an errorDetails object.
 * The syntax of a CALLERROR looks like this:
 *
 * [<MessageTypeId>, "<MessageId>", "<errorCode>", "<errorDescription>", {<errorDetails>}]
 *
 * For example, a CALLERROR could look like this:
 *
 * [4,
 *  "162376037",
 *  "NotSupported",
 *  "SetDisplayMessageRequest not implemented",
 *  {}
 * ]
 *
 */
public interface ICallError extends ICallResult<Object> {

    @Override
    default MessageType getMessageTypeId() {
        return MessageType.CALLERROR;
    }

    /**
     * This field must contain a string from the RPC Framework Error Codes table:
     *
     * ErrorCode:                       Description:
     * FormatViolation                  Payload for Action is syntactically incorrect
     * GenericError                     Any other error not covered by the more specific error codes in this table
     * InternalError                    An internal error occurred and the receiver was not able to process the requested Action successfully
     * MessageTypeNotSupported          A message with an Message Type Number received that is not supported by this implementation.
     * NotImplemented                   Requested Action is not known by receiver
     * NotSupported                     Requested Action is recognized but not supported by the receiver
     * OccurrenceConstraintViolation    Payload for Action is syntactically correct but at least one of the fields violates occurrence constraints
     * PropertyConstraintViolation      Payload is syntactically correct but at least one field contains an invalid value
     * ProtocolError                    Payload for Action is not conform the PDU structure
     * RpcFrameworkError                Content of the call is not a valid RPC Request, for example: MessageId could not be read.
     * SecurityError                    During the processing of Action a security issue occurred preventing receiver from completing the Action successfully
     * TypeConstraintViolation          Payload for Action is syntactically correct but at least one of the fields violates data type constraints (e.g. "somestring": 12)
     *
     * @return
     */
    String getErrorCode();

    /**
     * Should be filled in if possible, otherwise a clear empty string "".
     *
     * Length: String[255]
     * @return
     */
    String getErrorDescription();

    /**
     * This JSON object describes error details in an undefined way. If there are no error details you
     * MUST fill in an empty object {}.
     *
     * Datatype: JSON
     * @return
     */
    String getErrorDetails();

    @Override
    default Object getPayload() {
        throw new UnsupportedOperationException("A CallError does not contain a payload field.");
    }
}
