package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;

import java.util.Collection;

public interface ISessionManager {

    void registerSession(String csId, IOCPPSession session);

    IOCPPSession getSession(String csId);

    Collection<IOCPPSession> getSessions();

    Collection<String> getSessionIds();

    boolean sessionExists(String csId);
}
