package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.MessageRoutingMap;

/**
 * This interface defines the Charging Station Client API.
 *
 * The operations in this interface represents the requests that a Charging Station can invoke on
 * a Charging Station Management System.
 *
 * The interface consists of sub-interfaces, each mapping to a functional block in the OCPP 2.0.1 specification.
 *
 * For example:
 * Provisioning API
 * SmartCharging API
 * Authentication API
 * etc.
 */
public interface IChargingStationClientApi {

    /**
     * Returns the API for the provisioning subsystem.
     * @return
     */
    IChargingStationProvisioningClientApi getProvisioningAPI();

    MessageRoutingMap getMessageRoutingMap();
}
