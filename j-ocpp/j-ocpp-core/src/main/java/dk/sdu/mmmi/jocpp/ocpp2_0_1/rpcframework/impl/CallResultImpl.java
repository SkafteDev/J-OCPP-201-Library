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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.MessageType;

public class CallResultImpl<T> implements ICallResult<T> {
    private final T payload;
    private String messageId;

    public CallResultImpl(T payload, String messageId) {
        this.payload = payload;
        this.messageId = messageId;
    }

    protected CallResultImpl(String messageId) {
        this(null, messageId);
    }

    @Override
    public MessageType getMessageTypeId() {
        return MessageType.CALLRESULT;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    public static <T> CallResultMessageBuilder<T> newBuilder() {
        return new CallResultMessageBuilder<>();
    }


    public static class CallResultMessageBuilder<T> {
        private T payload;
        private String messageId;

        public CallResultMessageBuilder<T> withPayLoad(T payload) {
            this.payload = payload;
            return this;
        }

        /**
         * Provide the message id that corresponds to the call this CallResult is servicing.
         * @param messageId
         * @return
         */
        public CallResultMessageBuilder<T> withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public ICallResult<T> build() {
            if (payload == null) {
                throw new NullPointerException("The provided payload must not be null. Provide a valid payload.");
            }

            if (messageId == null) {
                throw new NullPointerException("The message id must not be null. Provide a valid message id.");
            }

            return new CallResultImpl<>(payload, messageId);
        }
    }

    @Override
    public String toString() {
        return "CallResultMessageImpl{" +
                "payload=" + payload +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
