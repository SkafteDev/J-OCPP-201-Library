package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;

public interface ICSClient {
    void connect();
    ICsmsServiceEndpoint getCsmsEndpoint();

    ICsServiceEndpoint getCsEndpoint();
}
