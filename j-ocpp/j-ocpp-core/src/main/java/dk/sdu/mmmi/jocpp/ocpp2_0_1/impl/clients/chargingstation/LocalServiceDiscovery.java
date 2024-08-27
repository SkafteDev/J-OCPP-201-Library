package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton service registry class to register and lookup local instances of CSMS and CS.
 */
public class LocalServiceDiscovery {
    private static LocalServiceDiscovery instance;

    private final Map<String, ICsmsService> csmsServices = new HashMap<>();
    private final Map<String, ICsServiceEndpoint> csEndpoints = new HashMap<>();
    private final Map<String, ICsmsServiceEndpoint> csmsEndpoints = new HashMap<>();

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
    public void registerCsEndpoint(String csId, ICsServiceEndpoint csEndpoint) {
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
    public void unregisterCsEndpoint(String csId) {
        this.csEndpoints.remove(csId);
    }


    /**
     * Returns the CS endpoint associated with the ID.
     *
     * Returns null if the ID is not registered.
     * @param csId
     * @return
     */
    public ICsServiceEndpoint getCsEndpoint(String csId) {
        if (!csEndpoints.containsKey(csId)) {
            return null;
        }

        return csEndpoints.get(csId);
    }



    /**
     * Registers the CSMS endpoint by the provided CS ID.
     *
     * Throws a RuntimeException if the ID is already in use.
     * @param csId
     * @param csmsEndpoint
     */
    public void registerCsmsEndpoint(String csId, ICsmsServiceEndpoint csmsEndpoint) {
        if (csmsEndpoints.containsKey(csId)) {
            throw new RuntimeException(String.format("CS id already used: %s", csId));
        }

        this.csmsEndpoints.put(csId, csmsEndpoint);
    }

    /**
     * Unregisters the CSMS endpoint associated by the CS ID.
     *
     * Has no effect if the ID is not registered.
     * @param csId
     */
    public void unregisterCsmsEndpoint(String csId) {
        this.csmsEndpoints.remove(csId);
    }


    /**
     * Returns the CSMS endpoint associated with the CS ID.
     *
     * Returns null if the ID is not registered.
     * @param csId
     * @return
     */
    public ICsmsServiceEndpoint getCsmsEndpoint(String csId) {
        if (!csmsEndpoints.containsKey(csId)) {
            return null;
        }

        return csmsEndpoints.get(csId);
    }
}
