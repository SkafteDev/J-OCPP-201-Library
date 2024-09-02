package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.sessions.ChargingStationNatsIoClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerContextLoader;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionManagerImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.net.URL;
import java.util.*;

/**
 * This example demonstrates the use case of:
 *
 * 1) Sending a BootNotificationRequest from CS -> CSMS.
 * 2) Receiving SetProfileChargingProfileRequest from CSMS -> CS
 *
 * Prerequisites:
 * 1) The machine must be able to connect to a NATS.io server.
 * 2) A CSMS over NATS.io implementation must be ready to receive requests from the CS.
 *
 */
public class ChargingStationOverNatsDemo {

    private static final String quitToken = "q";

    public static void main(String[] args) {
        /*
         * (1) Retrieve the configuration for communicating between CS <-> CSMS.
         */
        String csId = "f8125503-8d0f-467f-abad-b830ca6782e2"; // This has to be hard coded or configured directly on the charging station's firmware...
        URL resource = ClassLoader.getSystemResource("brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());

        /*
         * (2) Instantiate the CS client API to communicate between CS <-> CSMS.
         *     Connect the CS client to the CSMS before sending requests.
         */
        ISessionManager sessionManager = new SessionManagerImpl();
        IOCPPSession ocppSession = ChargingStationNatsIoClient.newBuilder()
                .withBrokerContext(brokerContext)
                .withCsId(csId)
                .withCsServiceInterface(new CSEndpoint(csId, sessionManager))
                .withSessionManager(sessionManager)
                .build();

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
