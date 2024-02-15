package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.IChargingStationApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.provisioning.IChargingStationProvisioningApi;

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
