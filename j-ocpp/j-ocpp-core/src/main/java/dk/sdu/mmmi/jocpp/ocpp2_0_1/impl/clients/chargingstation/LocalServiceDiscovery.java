package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IChargingStationServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton service registry class to register and lookup local instances of CSMS and CS.
 */
public class LocalServiceDiscovery {
    private static LocalServiceDiscovery instance;

    private final Map<String, ICsmsService> csmsServices = new HashMap<>();
    private final Map<String, IChargingStationServiceEndpoint> csEndpoints = new HashMap<>();

    private LocalServiceDiscovery() {
    }

    public static LocalServiceDiscovery getInstance() {
        // Lazy init on first call.
        if (instance == null) {
            instance = new LocalServiceDiscovery();
        }

        return instance;
    }

    /**
     * Registers the CSMS by the provided ID.
     *
     * Throws a RuntimeException if the ID is already in use.
     * @param csmsId
     * @param csms
     */
    public void registerCsms(String csmsId, ICsmsService csms) {
        if (csmsServices.containsKey(csmsId)) {
            throw new RuntimeException(String.format("CSMS id already used: %s", csmsId));
        }

        this.csmsServices.put(csmsId, csms);
    }

    /**
     * Unregisters the CSMS associated by the ID.
     *
     * Has no effect if the ID is not registered.
     * @param csmsId
     */
    public void unregisterCsms(String csmsId) {
        this.csmsServices.remove(csmsId);
    }


    /**
     * Returns the CSMS associated with the ID.
     *
     * Returns null if the ID is not registered.
     * @param csmsId
     * @return
     */
    public ICsmsService getCsms(String csmsId) {
        if (!csmsServices.containsKey(csmsId)) {
            return null;
        }

        return csmsServices.get(csmsId);
    }



    /**
     * Registers the CS endpoint by the provided ID.
     *
     * Throws a RuntimeException if the ID is already in use.
     * @param csId
     * @param csEndpoint
     */
    public void registerCsEndpoint(String csId, IChargingStationServiceEndpoint csEndpoint) {
        if (csEndpoints.containsKey(csId)) {
            throw new RuntimeException(String.format("CS id already used: %s", csId));
        }

        this.csEndpoints.put(csId, csEndpoint);
    }

    /**
     * Unregisters the CS associated by the ID.
     *
     * Has no effect if the ID is not registered.
     * @param csId
     */
    public void unregisterCs(String csId) {
        this.csEndpoints.remove(csId);
    }


    /**
     * Returns the CS endpoint associated with the ID.
     *
     * Returns null if the ID is not registered.
     * @param csId
     * @return
     */
    public IChargingStationServiceEndpoint getCsEndpoint(String csId) {
        if (!csEndpoints.containsKey(csId)) {
            return null;
        }

        return csEndpoints.get(csId);
    }
}
