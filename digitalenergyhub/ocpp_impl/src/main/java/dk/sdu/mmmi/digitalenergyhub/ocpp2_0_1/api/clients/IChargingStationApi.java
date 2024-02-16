package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.provisioning.IChargingStationProvisioningApi;

/**
 * This interface represents a Charging Station API in its entirety.
 *
 * This means that this interface must eventually contain all the functional blocks specified within the OCPP 2.0.1.
 * specification.
 */
public interface IChargingStationApi {

    /**
     * Returns the API for the provisioning subsystem.
     * @return
     */
    IChargingStationProvisioningApi getProvisioningAPI();
}
