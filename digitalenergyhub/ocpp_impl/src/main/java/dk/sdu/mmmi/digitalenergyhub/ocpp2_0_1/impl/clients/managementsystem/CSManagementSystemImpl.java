package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.IChargingStationManagementSystemApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.provisioning.ICSManagementSystemProvisioning;

public class CSManagementSystemImpl implements IChargingStationManagementSystemApi {

    private final ICSManagementSystemProvisioning managementSystemProvisioningApi;

    public CSManagementSystemImpl(ICSManagementSystemProvisioning provisioningApi) {
        this.managementSystemProvisioningApi = provisioningApi;
    }

    @Override
    public ICSManagementSystemProvisioning getProvisioningAPI() {
        return managementSystemProvisioningApi;
    }
}
