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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import java.util.Collection;

public interface ISessionManager {

    void registerSession(String csId, IOCPPSession session);

    IOCPPSession getSession(String csId);

    Collection<IOCPPSession> getSessions();

    Collection<String> getSessionIds();

    boolean sessionExists(String csId);

    void addListener(ISessionRegistrationListener listener);
    void removeListener(ISessionRegistrationListener listener);

    /*
     * Notification handlers
     */
    interface ISessionRegistrationListener {
        void onSessionRegistered(IOCPPSession session);
    }
}
