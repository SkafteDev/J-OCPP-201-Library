package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface IOCPPSession {
    void disconnect();
    ICsms getCsms();
    IChargingStation getChargingStation();

    SessionInfo getSessionInfo();

    interface SessionInfo {
        String getCsId();
        String getCsmsId();
        String getConnectionURI();
        String getTransportType();
        OcppVersion getOCPPVersion();
    }
}
