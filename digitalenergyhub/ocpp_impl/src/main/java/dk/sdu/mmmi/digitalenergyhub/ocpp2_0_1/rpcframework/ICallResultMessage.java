package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework;

/**
 * If the call can be handled correctly the result will be a regular CALLRESULT. Error situations that are covered by the definition of the
 * OCPP response definition are not considered errors in this context. They are regular results and as such will be treated as a normal
 * CALLRESULT, even if the result is undesirable for the recipient.
 * A CALLRESULT always consists of 3 elements: The standard elements MessageTypeId and MessageId and a payload, containing
 * the response to the Action in the original Call.
 *
 * The syntax of a CALLRESULT looks like this:
 * [<MessageTypeId>, "<MessageId>", {<Payload>}]
 *
 * For example, a BootNotification response could look like this:
 *
 * [3,
 *  "19223201",
 *  {
 *      "currentTime": "2013-02-01T20:53:32.486Z",
 *      "interval": 300,
 *      "status": "Accepted"
 *      }
 *  ]
 */
public interface ICallResultMessage extends IRPCMessage {

    @Override
    default MessageType getMessageTypeId() {
        return MessageType.CALLRESULT;
    }
}
