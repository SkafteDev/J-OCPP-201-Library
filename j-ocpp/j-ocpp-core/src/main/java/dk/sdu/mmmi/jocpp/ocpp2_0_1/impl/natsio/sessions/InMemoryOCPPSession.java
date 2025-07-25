
package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.sessions;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.LocalServiceDiscovery;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionInfoImpl;

public class InMemoryOCPPSession implements IOCPPSession {

    private ICsmsEndpoint csmsEndpoint;
    private ICsEndpoint csEndpoint;
    private final SessionInfoImpl sessionInfo;

    private InMemoryOCPPSession(String csId, String csmsId) {
        this.sessionInfo = SessionInfoImpl.SessionInfoImplBuilder.newBuilder()
                .withConnectionURI("N/A")
                .withTransportType("In-memory transport")
                .withOcppVersion(OcppVersion.OCPP_201)
                .withCsId(csId)
                .withCsmsId(csmsId)
                .build();
    }

    public static IOCPPSession connect(String csId, String csmsId, LocalServiceDiscovery serviceDiscovery) {
        InMemoryOCPPSession inMemoryOCPPSession = new InMemoryOCPPSession(csId, csmsId);

        inMemoryOCPPSession.csEndpoint = serviceDiscovery.getCs(csId);
        inMemoryOCPPSession.csmsEndpoint = serviceDiscovery.getCsms(csId);

        serviceDiscovery.getSessionManager(csmsId).registerSession(csId, inMemoryOCPPSession);

        return inMemoryOCPPSession;
    }

    public static IOCPPSession connect(String csId, String csmsId, ISessionManager sessionManager, ICsmsEndpoint csmsEndpoint, ICsEndpoint csEndpoint) {
        InMemoryOCPPSession inMemoryOCPPSession = new InMemoryOCPPSession(csId, csmsId);

        inMemoryOCPPSession.csEndpoint = csEndpoint;
        inMemoryOCPPSession.csmsEndpoint = csmsEndpoint;

        sessionManager.registerSession(csId, inMemoryOCPPSession);

        return inMemoryOCPPSession;
    }

    @Override
    public void disconnect() {
        this.csEndpoint = null;
        this.csmsEndpoint = null;
    }

    @Override
    public ICsmsEndpoint getCsms() {
        return csmsEndpoint;
    }

    @Override
    public ICsEndpoint getChargingStation() {
        return csEndpoint;
    }

    @Override
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }
}
