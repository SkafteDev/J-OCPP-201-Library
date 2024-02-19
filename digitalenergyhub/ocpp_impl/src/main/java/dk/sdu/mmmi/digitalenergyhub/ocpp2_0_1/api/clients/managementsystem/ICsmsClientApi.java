package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.provisioning.ICsmsClientProvisioningApi;

/**
 * This interface defines the Charging Station Management System Client API.
 *
 * The operations in this interface represents the requests that a Charging Station Management System can invoke on
 * a Charging Station.
 *
 * The interface consists of sub-interfaces, each mapping to a functional block in the OCPP 2.0.1 specification.
 *
 * For example:
 * Provisioning API
 * SmartCharging API
 * Authentication API
 * etc.
 */
public interface ICsmsClientApi {

    /**
     * Returns the API for the provisioning subsystem.
     * @return
     */
    ICsmsClientProvisioningApi getProvisioningAPI();
}
