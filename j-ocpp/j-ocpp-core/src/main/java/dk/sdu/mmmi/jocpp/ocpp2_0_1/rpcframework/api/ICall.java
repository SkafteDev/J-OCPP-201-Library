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
 * A CALL always consists of 4 elements: The standard elements MessageTypeId and MessageId, a specific Action that is required on
 * the other side and a payload, the arguments to the Action. The syntax of a CALL looks like this:
 * [<MessageTypeId>, "<MessageId>", "<Action>", {<Payload>}]
 *
 * For example, a BootNotificationRequest could look like this:
 *
 * [2,
 *  "19223201",
 *  "BootNotification",
 *  {
 *      "reason": "PowerUp",
 *      "chargingStation": {
 *      "model": "SingleSocketCharger",
 *      "vendorName": "VendorX"
 *      }
 *  }
 * ]
 */
public interface ICall<T> extends IRPCMessage<T> {

    @Override
    default MessageType getMessageTypeId() {
        return MessageType.CALL;
    }

    /**
     * The name of the remote procedure or action. This field SHALL contain a case-sensitive string.
     * The field SHALL contain the OCPP Message name without the "Request" suffix. For example: For
     * a "BootNotificationRequest", this field shall be set to "BootNotification".
     * @return
     */
    String getAction();
}
