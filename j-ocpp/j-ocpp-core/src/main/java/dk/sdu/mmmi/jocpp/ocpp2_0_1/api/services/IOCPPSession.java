package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface IOCPPSession {
    void disconnect();
    ICsmsServiceEndpoint getCsmsServiceEndpoint();
    ICsServiceEndpoint getCsServiceEndpoint();

    SessionInfo getSessionInfo();

    interface SessionInfo {
        enum ConnectionState {
            CONNECTING, CONNECTED, DISCONNECTED, RECONNECTING
        }

        ConnectionState getConnectionState();

        String getCsId();
        String getCsmsId();
        String getConnectionURI();
        String getTransportType();
        OcppVersion getOCPPVersion();
    }
}
