package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.application.csms.CsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation.OCPPSessionInMemory;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation.LocalServiceDiscovery;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.ChargingStation;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class ChargingStationOfflineDemo {
    private static final String quitToken = "q";

    // This is the ID used to locate the CSMS.
    private static final String CSMS_ID = "ewiicsms";

    // This is the ID used to locate the CS.
    // This must be hard coded or configured directly on the charging station's firmware...
    private static final String CS_ID = "f8125503-8d0f-467f-abad-b830ca6782e2";

    private static void registerServices() {
        ICsmsServiceEndpoint csmsEndpoint = new CsmsServiceEndpoint();

        LocalServiceDiscovery.getInstance().registerCsEndpoint(CS_ID, new CSServiceEndpointImpl());
        LocalServiceDiscovery.getInstance().registerCsmsEndpoint(CS_ID, csmsEndpoint);
    }


    public static void main(String[] args) {
        /**
         * (1) Register a reference to a local ICsmsService in a discovery service.
         */
        registerServices();

        /*
         * (2) Instantiate the CS client API to communicate between CS <-> CSMS.
         *     Connect the CS client to the CSMS before sending requests.
         */
        IOCPPSession csClient = OCPPSessionInMemory.connect(CS_ID, CSMS_ID, LocalServiceDiscovery.getInstance());

        /*
         * (3) Build the BootNotificationRequest (payload) object to be sent to the CSMS.
         */
        BootNotificationRequest bootNotificationRequest = BootNotificationRequest.builder()
                .withChargingStation(ChargingStation.builder()
                        .withVendorName("ABB")
                        .withSerialNumber("f8125503-8d0f-467f-abad-b830ca6782e2")
                        .withModel("ABB TAC-W11-G5-R-0")
                        .withFirmwareVersion("3.1.3")
                        .build())
                .withReason(BootReasonEnum.POWER_UP)
                .build();

        /*
         * (4) Encapsulate the BootNotificationRequest payload within an RPC CALL.
         */
        ICall<BootNotificationRequest> bootRequest = CallImpl.<BootNotificationRequest>newBuilder()
                .withMessageId(UUID.randomUUID().toString())
                .asAction(OCPPMessageType.BootNotificationRequest.getAction())
                .withPayLoad(bootNotificationRequest)
                .build();

        /*
         * (5) Send the BootNotificationRequest and block until receiving a BootNotificationResponse.
         */
        ICallResult<BootNotificationResponse> response = csClient.getCsmsServiceEndpoint().sendBootNotificationRequest(bootRequest);
        System.out.println(response);


        /*
         * (6) Prevent the program for exiting to simulate that the CS is now awaiting requests from the CSMS.
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
