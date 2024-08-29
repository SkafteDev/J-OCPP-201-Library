package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface IHandshakeResponse {
    /*
     * The result of the handshake
     */
    HandshakeResult getHandshakeResult();

    /*
     * The endpoint used to exchange messages.
     */
    String getEndPoint();

    OcppVersion getOcppVersion();

    /**
     * Optional description of the response.
     * E.g. an explanation of why a connection was rejected.
     */
    String getReason();
}
