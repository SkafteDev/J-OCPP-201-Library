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
public interface ICallResult<T> extends IRPCMessage<T> {

    @Override
    default MessageType getMessageTypeId() {
        return MessageType.CALLRESULT;
    }
}
