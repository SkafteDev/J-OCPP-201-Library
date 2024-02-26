package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;

public class CallResultMessageImpl<T> implements ICallResultMessage<T> {
    private final T payload;
    private String messageId;

    public CallResultMessageImpl(T payload, String messageId) {
        this.payload = payload;
        this.messageId = messageId;
    }

    protected CallResultMessageImpl(String messageId) {
        this.payload = null;
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

        public ICallResultMessage<T> build() {
            if (payload == null) {
                throw new NullPointerException("The provided payload must not be null. Provide a valid payload.");
            }

            if (messageId == null) {
                throw new NullPointerException("The message id must not be null. Provide a valid message id.");
            }

            return new CallResultMessageImpl<>(payload, messageId);
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
