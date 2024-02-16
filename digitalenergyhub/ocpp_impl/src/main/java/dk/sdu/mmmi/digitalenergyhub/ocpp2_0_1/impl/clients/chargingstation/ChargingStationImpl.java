package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.IChargingStationApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.provisioning.IChargingStationProvisioningApi;

public class ChargingStationImpl implements IChargingStationApi {

    private final IChargingStationProvisioningApi provisioningApi;

    public ChargingStationImpl(IChargingStationProvisioningApi provisioningApi) {
        this.provisioningApi = provisioningApi;
    }

    @Override
    public IChargingStationProvisioningApi getProvisioningAPI() {
        return provisioningApi;
    }
}
