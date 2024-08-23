package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.ICSClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;

public class ChargingStationLocalClient implements ICSClient {

    private final String csId;
    private ICsmsServiceEndpoint csmsEndpoint;
    private final ICsmsService csmsService;
    private final ICsServiceEndpoint csEndpoint;

    public ChargingStationLocalClient(String csIdentity, String csmsId, ICsServiceEndpoint csEndpoint) {
        this.csId = csIdentity;
        this.csmsService = LocalServiceDiscovery.getInstance().getCsms(csmsId);
        this.csEndpoint = csEndpoint;
    }

    @Override
    public void connect() {
        // Connect only if not already connected.
        if (csmsEndpoint != null) return;

        ICsmsService.HandshakeRequest handshakeReq = HandshakeRequestImpl.HandshakeInfoImplBuilder
                .newBuilder()
                .withIdentity(csId)
                .withOcppVersion(HandshakeOcppVersion.OCPP_201)
                .build();

        this.csmsEndpoint = csmsService.connect(handshakeReq);

        // Upon connection, register the CS endpoint for the CSMS to discover it later.
        LocalServiceDiscovery.getInstance().registerCsEndpoint(csId, csEndpoint);
    }

    @Override
    public ICsmsServiceEndpoint getCsmsEndpoint() {
        return this.csmsEndpoint;
    }

    @Override
    public ICsServiceEndpoint getCsEndpoint() {
        return this.csEndpoint;
    }
}
