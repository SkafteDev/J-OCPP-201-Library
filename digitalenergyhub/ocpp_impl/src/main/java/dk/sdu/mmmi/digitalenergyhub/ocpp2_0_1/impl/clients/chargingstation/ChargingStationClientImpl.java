package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning.IChargingStationProvisioningClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;

public class ChargingStationClientImpl implements IChargingStationClientApi {

    private IChargingStationProvisioningClientApi provisioningClientApi;
    private final MessageRoutingMapImpl messageRoutingMapImpl;

    public ChargingStationClientImpl(MessageRoutingMapImpl routingMap) {
        this.messageRoutingMapImpl = routingMap;
    }

    public void add(IChargingStationProvisioningClientApi provisioningApi) {
        this.provisioningClientApi = provisioningApi;
    }

    @Override
    public IChargingStationProvisioningClientApi getProvisioningAPI() {
        return provisioningClientApi;
    }

    @Override
    public MessageRoutingMapImpl getMessageRoutingMap() {
        return this.messageRoutingMapImpl;
    }
}
