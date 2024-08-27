package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.OcppVersion;

public class SessionInfoImpl implements IOCPPSession.SessionInfo {
    ConnectionState connectionState = ConnectionState.DISCONNECTED;
    String csId = null;
    String csmsId = null;
    String connectionURI = null;
    String transportType = null;
    OcppVersion ocppVersion;

    @Override
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    @Override
    public String getCsId() {
        return csId;
    }

    @Override
    public String getCsmsId() {
        return csmsId;
    }

    @Override
    public String getConnectionURI() {
        return connectionURI;
    }

    @Override
    public String getTransportType() {
        return transportType;
    }

    @Override
    public OcppVersion getOCPPVersion() {
        return ocppVersion;
    }

}
