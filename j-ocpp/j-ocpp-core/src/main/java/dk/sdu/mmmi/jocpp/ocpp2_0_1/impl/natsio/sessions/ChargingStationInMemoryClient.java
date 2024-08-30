package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.sessions;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.LocalServiceDiscovery;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionInfoImpl;

public class ChargingStationInMemoryClient implements IOCPPSession {

    private ICsmsEndpoint csmsService;
    private ICsEndpoint csService;
    private final SessionInfoImpl sessionInfo;

    private ChargingStationInMemoryClient(String csId, String csmsId) {
        this.sessionInfo = SessionInfoImpl.SessionInfoImplBuilder.newBuilder()
                .withConnectionURI("N/A")
                .withTransportType("In-memory transport")
                .withOcppVersion(OcppVersion.OCPP_201)
                .withCsId(csId)
                .withCsmsId(csmsId)
                .build();
    }

    public static IOCPPSession connect(String csId, String csmsId, LocalServiceDiscovery serviceDiscovery) {
        ChargingStationInMemoryClient chargingStationInMemoryClient = new ChargingStationInMemoryClient(csId, csmsId);

        chargingStationInMemoryClient.csService = serviceDiscovery.getCs(csId);
        chargingStationInMemoryClient.csmsService = serviceDiscovery.getCsms(csId);

        serviceDiscovery.getSessionManager(csmsId).registerSession(csId, chargingStationInMemoryClient);

        return chargingStationInMemoryClient;
    }

    @Override
    public void disconnect() {
        this.csService = null;
        this.csmsService = null;
    }

    @Override
    public ICsmsEndpoint getCsms() {
        return csmsService;
    }

    @Override
    public ICsEndpoint getChargingStation() {
        return csService;
    }

    @Override
    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }
}
