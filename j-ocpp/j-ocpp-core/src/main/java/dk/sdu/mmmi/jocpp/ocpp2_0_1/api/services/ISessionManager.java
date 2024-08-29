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
