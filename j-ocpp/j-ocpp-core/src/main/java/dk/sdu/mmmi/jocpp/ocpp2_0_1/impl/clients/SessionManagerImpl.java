package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;

import java.util.*;

public class SessionManagerImpl implements ISessionManager {

    // Key = csId.
    private final Map<String, IOCPPSession> sessions;

    public SessionManagerImpl() {
        this.sessions = new HashMap<>();
    }

    @Override
    public void registerSession(String csId, IOCPPSession session) {
        if (sessions.containsKey(csId)) {
            return;
        }

        sessions.put(csId, session);
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
}
