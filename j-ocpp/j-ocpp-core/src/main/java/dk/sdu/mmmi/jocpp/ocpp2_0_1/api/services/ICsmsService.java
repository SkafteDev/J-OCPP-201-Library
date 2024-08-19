package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface ICsmsService {
    ICsmsServiceEndpoint connect(HandshakeRequest handshakeRequest);

    interface HandshakeRequest {

        /**
         * The identity of the CS.
         */
        String getIdentity();


        HandshakeOcppVersion getOcppVersion();
    }

    interface HandshakeResponse {
        /*
         * The result of the handshake
         */
        HandshakeResult getHandshakeResult();

        /*
         * The endpoint used to exchange messages.
         */
        String getEndPoint();

        HandshakeOcppVersion getOcppVersion();

        /**
         * Optional description of the response.
         * E.g. an explanation of why a connection was rejected.
         */
        String getReason();
    }
}
