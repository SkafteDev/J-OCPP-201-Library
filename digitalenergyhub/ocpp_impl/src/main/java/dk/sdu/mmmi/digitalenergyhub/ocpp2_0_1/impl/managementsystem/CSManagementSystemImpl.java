package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.IChargingStationManagementSystemApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.provisioning.ICSManagementSystemProvisioning;

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
