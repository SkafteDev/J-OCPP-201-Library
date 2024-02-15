package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This is the general Remote Procedure Call (RPC) Message exchanged between parties.
 */
public interface IRPCMessage<T> {

    /**
     * To identify the type of message one of the following Message Type Numbers MUST be used.
     *
     * CALL = 2 (Request message)
     * CALLRESULT = 3 (ResponseMessage)
     * CALLERROR = 4 (Error response to a request message)
     * @return
     */
    MessageType getMessageTypeId();

    /**
     * The message ID serves to identify a request. A message ID for any CALL message MUST be different from all message IDs
     * previously used by the same sender for any other CALL messages on the same WebSocket connection. A message ID for a
     * CALLRESULT or CALLERROR message MUST be equal to that of the CALL message that the CALLRESULT or CALLERROR message
     * is a response to.
     *
     * @return Unique message ID, maximum length of 36 characters, to allow for UUIDs/GUIDs.
     */
    String getMessageId();

    /**
     * JSON Payload.
     *
     * The Payload of a message is a JSON object containing the arguments relevant to the Action.
     *
     * @return
     */
    T getPayload();
}
