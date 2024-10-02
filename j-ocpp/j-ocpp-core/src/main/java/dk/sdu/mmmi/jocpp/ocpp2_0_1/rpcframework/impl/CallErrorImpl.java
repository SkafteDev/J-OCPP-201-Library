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

import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.MessageType;

public class CallErrorImpl extends CallResultImpl implements ICallError {

    private String errorCode;
    private String errorDescription;
    private String errorDetails;

    public CallErrorImpl(String messageId) {
        super(messageId);
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String getErrorDetails() {
        return errorDetails;
    }

    @Override
    public MessageType getMessageTypeId() {
        return MessageType.CALLERROR;
    }

    public static CallErrorBuilder newCallErrorBuilder() {
        return new CallErrorBuilder();
    }


    public static class CallErrorBuilder {

        private ErrorCode errorCode;
        private String errorDescription;
        private String errorDetails;
        private String messageId;

        /**
         * Provide the message id that corresponds to the call this CallError is servicing.
         * @param messageId
         * @return
         */
        public CallErrorBuilder withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public CallErrorBuilder withErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        /**
         * A string object of maximum length 255.
         * @param errorDescription
         * @return
         */
        public CallErrorBuilder withErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        /**
         * This field MUST be a valid JSON object.
         * @param errorDetails
         * @return
         */
        public CallErrorBuilder withErrorDetails(String errorDetails) {
            this.errorDetails = errorDetails;
            return this;
        }

        public ICallError build() {
            if (this.messageId == null) {
                throw new NullPointerException("A call result MUST have a message id corresponding to the call it is " +
                        "servicing.");
            }

            CallErrorImpl callError = new CallErrorImpl(messageId);
            callError.errorCode = this.errorCode.getValue();

            if (errorDescription == null) {
                callError.errorDescription = "";
            } else {
                callError.errorDescription = this.errorDescription;
            }

            if (errorDetails == null || errorDetails.isEmpty()) {
                callError.errorDetails = "{}";
            } else {
                callError.errorDetails = this.errorDetails;
            }

            return callError;
        }
    }

    @Override
    public String toString() {
        return "CallErrorImpl{" +
                "errorCode='" + errorCode + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorDetails='" + errorDetails + '\'' +
                "} " + super.toString();
    }
}
