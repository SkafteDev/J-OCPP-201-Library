
package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;

import java.util.*;

public class SessionManagerImpl implements ISessionManager {

    // Key = csId.
    private final Map<String, IOCPPSession> sessions;

    private final List<ISessionRegistrationListener> sessionRegistrationListeners;

    public SessionManagerImpl() {
        this.sessions = new HashMap<>();
        this.sessionRegistrationListeners = new LinkedList<>();
    }

    @Override
    public void registerSession(String csId, IOCPPSession session) {
        if (sessions.containsKey(csId)) {
            return;
        }

        sessions.put(csId, session);
        onSessionRegistered(session);
    }

    @Override
    public IOCPPSession getSession(String csId) {
        return sessions.get(csId);
    }

    @Override
    public Collection<IOCPPSession> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public Collection<String> getSessionIds() {
        return new HashSet<>(sessions.keySet());
    }

    @Override
    public boolean sessionExists(String csId) {
        return sessions.containsKey(csId);
    }

    @Override
    public void addListener(ISessionRegistrationListener listener) {
        if (sessionRegistrationListeners.contains(listener)) {
            return;
        }

        sessionRegistrationListeners.add(listener);
    }

    @Override
    public void removeListener(ISessionRegistrationListener listener) {
        sessionRegistrationListeners.remove(listener);
    }

    private void onSessionRegistered(IOCPPSession session) {
        for (ISessionRegistrationListener l : sessionRegistrationListeners) {
            l.onSessionRegistered(session);
        }
    }
}
