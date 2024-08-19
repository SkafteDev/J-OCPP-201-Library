package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface ICsmsService {
    ICsmsServiceEndpoint connect(HandshakeRequest handshakeRequest);

    interface HandshakeRequest {

        /**
         * The identity of the CS.
         * @return
         */
        String getIdentity();

        /*
         * Valid values as defined in 'OCPP 2.0.1: Part 4 - JSON over WebSockets implementation guide':
         *
         * ocpp1.2
         * ocpp1.5
         * ocpp1.6
         * ocpp2.0
         * ocpp2.0.1
         */
        String getOcppVersion();
    }

    interface HandshakeResponse {

        /*
         * The endpoint used to exchange messages.
         */
        String getEndPoint();

        /**
         * The result of the handshake:
         * Accepted
         * Rejected
         */
        String getHandshakeResult();

        /**
         * Optional description of the response.
         * E.g. an explanation of why a connection was rejected.
         */
        String getDescription();
    }
}
