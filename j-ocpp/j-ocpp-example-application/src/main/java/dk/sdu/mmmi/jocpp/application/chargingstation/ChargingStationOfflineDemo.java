
package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.application.csms.CsmsController;
import dk.sdu.mmmi.jocpp.application.csms.CsmsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.sessions.InMemoryOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionManagerImpl;

import java.time.Duration;
import java.util.*;

public class ChargingStationOfflineDemo {
    private static final String quitToken = "q";

    // This is the ID used to locate the CSMS.
    private static final String CSMS_ID = "ewiicsms";

    // This is the ID used to locate the CS.
    // This must be hard coded or configured directly on the charging station's firmware...
    private static final String CS_ID = "f8125503-8d0f-467f-abad-b830ca6782e2";

    public static void main(String[] args) {
        /**
         * (1) Register a reference to a local CSMS.
         */
        ISessionManager sessionManager = new SessionManagerImpl();
        sessionManager.addListener(session -> System.out.println(String.format("New session established: %s", session.getSessionInfo())));
        CsmsEndpoint csmsEndpoint = new CsmsEndpoint(CSMS_ID, sessionManager);
        CsmsController csmsController = new CsmsController(sessionManager);
        new Thread(() -> {
            csmsController.startSmartChargingControlLoop(Duration.ofSeconds(15));
        }).start();

        /*
         * (2) Instantiate the CS client API to communicate between CS <-> CSMS.
         *     Connect the CS client to the CSMS before sending requests.
         */
        ICsEndpoint csEndpoint = new CSEndpoint(CS_ID, sessionManager);
        IOCPPSession ocppSession = InMemoryOCPPSession.connect(CS_ID, CSMS_ID, sessionManager, csmsEndpoint, csEndpoint);

        // (3) Run the CS controller (i.e. the logic of the CS).
        CsController csController = new CsController(ocppSession);
        csController.run();

        /*
         * (4) Prevent the program for exiting to simulate that the CS is now awaiting requests from the CSMS.
         */
        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }
}
