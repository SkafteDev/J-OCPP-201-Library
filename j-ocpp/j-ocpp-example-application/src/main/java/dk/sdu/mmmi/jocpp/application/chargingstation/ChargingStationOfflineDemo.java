package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.application.csms.CsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.clients.ICSClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsService;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsmsServiceEndpoint;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation.ChargingStationLocalClient;
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

    public static void main(String[] args) {
        /**
         * (1) Register a reference to a local ICsmsService in a discovery service.
         */
        String csmsId = "ewiicsms"; // This can be retrieved from a local config. This value is looked up in the LocalServiceDiscovery.
        ICsmsService csmsService = new ICsmsService() {
            @Override
            public ICsmsServiceEndpoint connect(HandshakeRequest handshakeRequest) {
                return new CsmsServiceEndpoint(handshakeRequest);
            }
        };
        LocalServiceDiscovery.getInstance().registerCsms(csmsId, csmsService);

        /*
         * (2) Instantiate the CS client API to communicate between CS <-> CSMS.
         */
        String csId = "f8125503-8d0f-467f-abad-b830ca6782e2"; // This has to be hard coded or configured directly on the charging station's firmware...
        ICSClient csNatsClient = new ChargingStationLocalClient(csId, csmsId, new CSServiceEndpointImpl());

        // Connect the CS client to the CSMS before sending requests.
        csNatsClient.connect();

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
        ICallResult<BootNotificationResponse> response = csNatsClient.getCsmsEndpoint().sendBootNotificationRequest(bootRequest);
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
