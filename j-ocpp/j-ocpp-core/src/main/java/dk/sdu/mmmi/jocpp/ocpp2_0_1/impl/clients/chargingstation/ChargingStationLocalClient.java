package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.ICSClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.IRequestHandlerRegistry;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPRequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.*;

public class ChargingStationLocalClient implements ICSClient {

    private final String csId;
    private ICsmsServiceEndpoint csmsEndpoint;
    private final ICsmsService csmsService;
    private final IChargingStationServiceEndpoint csEndpoint;

    private IRequestHandlerRegistry csRequestHandlerMock;

    public ChargingStationLocalClient(String csIdentity, String csmsId, IChargingStationServiceEndpoint csEndpoint) {
        this.csId = csIdentity;
        this.csmsService = LocalServiceDiscovery.getInstance().getCsms(csmsId);
        this.csRequestHandlerMock = createMock();
        this.csEndpoint = csEndpoint;
    }

    private IRequestHandlerRegistry createMock() {
        return new IRequestHandlerRegistry() {
            @Override
            public <TRequest, TResponse> void addRequestHandler(OCPPMessageType msgType, OCPPRequestHandler<TRequest, TResponse> requestHandler) {
                // Do nothing...
            }

            @Override
            public void removeRequestHandler(OCPPMessageType msgType) {
                // Do nothing...
            }

            @Override
            public boolean existsRequestHandler(OCPPMessageType msgType) {
                return false;
            }

            @Override
            public IMessageRouteResolver getMsgRouteResolver() {
                return null;
            }
        };
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
    public ICsmsServiceEndpoint getEndpoint() {
        return this.csmsEndpoint;
    }

    @Override
    public IRequestHandlerRegistry getCsService() {
        return this.csRequestHandlerMock;
    }
}
