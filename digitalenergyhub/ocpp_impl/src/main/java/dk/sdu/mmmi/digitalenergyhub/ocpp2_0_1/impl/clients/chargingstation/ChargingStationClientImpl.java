package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning.IChargingStationProvisioningClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.MessageRoutingMap;

public class ChargingStationClientImpl implements IChargingStationClientApi {

    private IChargingStationProvisioningClientApi provisioningClientApi;
    private final MessageRoutingMap messageRoutingMap;

    public ChargingStationClientImpl(MessageRoutingMap routingMap) {
        this.messageRoutingMap = routingMap;
    }

    public void add(IChargingStationProvisioningClientApi provisioningApi) {
        this.provisioningClientApi = provisioningApi;
    }

    @Override
    public IChargingStationProvisioningClientApi getProvisioningAPI() {
        return provisioningClientApi;
    }

    @Override
    public MessageRoutingMap getMessageRoutingMap() {
        return this.messageRoutingMap;
    }
}
