package dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;

import java.util.UUID;

public class CallImpl<T> implements ICall<T> {
    private final T payload;
    private final String action;
    private final String messageId;

    public CallImpl(T payload, String action, String messageId) {
        this.payload = payload;
        this.action = action;
        this.messageId = messageId;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    public static <T> CallMessageBuilder<T> newBuilder() {
        return new CallMessageBuilder<T>();
    }


    public static class CallMessageBuilder<T> {

        private T payload;
        private String action;

        private String messageId;

        public CallMessageBuilder<T> withPayLoad(T payload) {
            this.payload = payload;
            return this;
        }

        public CallMessageBuilder<T> withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * The name of the remote procedure or action. This field SHALL contain a case-sensitive string.
         * The field SHALL contain the OCPP Message name without the "Request" suffix. For example: For
         * a "BootNotificationRequest", this field shall be set to "BootNotification".
         * @param action
         * @return
         */
        public CallMessageBuilder<T> asAction(String action) {
            this.action = action;
            return this;
        }

        public ICall<T> build() {
            if (payload == null) {
                throw new NullPointerException("The provided payload must not be null. Provide a valid payload.");
            }

            if (action == null) {
                throw new NullPointerException("The provided action must not be null. Provide a valid action.");
            }

            if (messageId == null) {
                messageId = UUID.randomUUID().toString();
            }

            return new CallImpl<>(payload, action, messageId);
        }
    }
}
