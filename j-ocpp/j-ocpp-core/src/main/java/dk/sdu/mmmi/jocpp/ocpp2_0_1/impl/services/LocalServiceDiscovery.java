/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton service registry class to register and lookup local instances of CSMS and CS.
 */
public class LocalServiceDiscovery {
    private static LocalServiceDiscovery instance;

    private final Map<String, ICsmsEndpoint> csmsServices = new HashMap<>();
    private final Map<String, ICsEndpoint> csServices = new HashMap<>();

    private final Map<String, ISessionManager> sessionManagers = new HashMap<>();

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
    public void registerCsms(String csmsId, ICsmsEndpoint csms) {
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
    public ICsmsEndpoint getCsms(String csmsId) {
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
     * @param csService
     */
    public void registerCs(String csId, ICsEndpoint csService) {
        if (csServices.containsKey(csId)) {
            throw new RuntimeException(String.format("CS id already used: %s", csId));
        }

        this.csServices.put(csId, csService);
    }

    /**
     * Unregisters the CS associated by the ID.
     *
     * Has no effect if the ID is not registered.
     * @param csId
     */
    public void unregisterCs(String csId) {
        this.csServices.remove(csId);
    }


    /**
     * Returns the CS endpoint associated with the ID.
     *
     * Returns null if the ID is not registered.
     * @param csId
     * @return
     */
    public ICsEndpoint getCs(String csId) {
        if (!csServices.containsKey(csId)) {
            return null;
        }

        return csServices.get(csId);
    }

    public void registerSessionManager(String sessionManagerId, ISessionManager sessionManager) {
        if (sessionManagers.containsKey(sessionManagerId)) {
            throw new RuntimeException(String.format("Session manager id already used: %s", sessionManagerId));
        }

        this.sessionManagers.put(sessionManagerId, sessionManager);
    }

    public void unregisterSessionManager(String sessionManagerId) {
        this.sessionManagers.remove(sessionManagerId);
    }

    public ISessionManager getSessionManager(String sessionManagerId) {
        if (!sessionManagers.containsKey(sessionManagerId)) {
            return null;
        }

        return sessionManagers.get(sessionManagerId);
    }
}
