package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;

public class OCPPSessionInMemory implements IOCPPSession {

    private final LocalServiceDiscovery serviceDiscovery;
    private ICsmsServiceEndpoint csmsEndpoint;
    private ICsServiceEndpoint csEndpoint;
    private final SessionInfoImpl sessionInfo;

    public OCPPSessionInMemory(String csId, String csmsId, LocalServiceDiscovery serviceDiscovery) {
        this.sessionInfo = new SessionInfoImpl();
        this.sessionInfo.connectionState = SessionInfo.ConnectionState.DISCONNECTED;
        this.sessionInfo.connectionURI = "N/A";
        this.sessionInfo.transportType = "In-memory transport";
        this.sessionInfo.ocppVersion = OcppVersion.OCPP_201;
        this.sessionInfo.csId = csId;
        this.sessionInfo.csmsId = csmsId;
        this.serviceDiscovery = serviceDiscovery;
    }

    public static IOCPPSession connect(String csId, String csmsId, LocalServiceDiscovery serviceDiscovery) {
        OCPPSessionInMemory ocppSessionInMemory = new OCPPSessionInMemory(csId, csmsId, serviceDiscovery);

        ocppSessionInMemory.sessionInfo.connectionState = SessionInfo.ConnectionState.CONNECTING;
        ocppSessionInMemory.csEndpoint = serviceDiscovery.getCsEndpoint(ocppSessionInMemory.sessionInfo.getCsId());
        ocppSessionInMemory.csmsEndpoint = serviceDiscovery.getCsmsEndpoint(ocppSessionInMemory.sessionInfo.getCsId());

        ocppSessionInMemory.sessionInfo.connectionState = SessionInfo.ConnectionState.CONNECTED;

        return ocppSessionInMemory;
    }

    @Override
    public void disconnect() {
        if (sessionInfo.getConnectionState() != SessionInfo.ConnectionState.CONNECTED) {
            return;
        }

        this.csEndpoint = null;
        this.csmsEndpoint = null;

        sessionInfo.connectionState = SessionInfo.ConnectionState.DISCONNECTED;
    }

    @Override
    public ICsmsServiceEndpoint getCsmsServiceEndpoint() {
        return csmsEndpoint;
    }

    @Override
    public ICsServiceEndpoint getCsServiceEndpoint() {
        return csEndpoint;
    }

    @Override
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }
}
