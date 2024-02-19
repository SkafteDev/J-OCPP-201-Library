package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.ICsmsClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.provisioning.ICsmsClientProvisioningApi;

public class CsmsClientImpl implements ICsmsClientApi {

    private final ICsmsClientProvisioningApi provisioningApi;

    public CsmsClientImpl(ICsmsClientProvisioningApi provisioningApi) {
        this.provisioningApi = provisioningApi;
    }

    @Override
    public ICsmsClientProvisioningApi getProvisioningAPI() {
        return provisioningApi;
    }
}
