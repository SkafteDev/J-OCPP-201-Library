package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.application.chargingstation.requesthandlers.SetChargingProfileRequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation.ChargingStationApi;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerContextLoader;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.ChargingStation;

import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 * This example demonstrates the use case of:
 *
 * 1) Sending a BootNotificationRequest from CS -> CSMS.
 * 2) Receiving SetProfileChargingProfileRequest from CSMS -> CS
 */
public class ChargingStationDemo {

    private static final String quitToken = "q";

    public static void main(String[] args) {
        /*
         * (1) Instantiate the API to communicate between CS <-> CSMS.
         */
        String csId = "f8125503-8d0f-467f-abad-b830ca6782e2"; // This has to be hard coded or configured directly on the charging station's firmware...
        URL resource = ClassLoader.getSystemResource("brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());

        ChargingStationApi csApi = ChargingStationApi.newBuilder()
                .withBrokerContext(brokerContext)
                .withCsId(csId)
                .build();

        /*
         * (2) Add request handlers for the requests this charging station can handle.
         *     In this example, the CS can only handle SetChargingProfileRequest.
         */
        csApi.getChargingStationServer().addRequestHandler(
                OCPPMessageType.SetChargingProfileRequest,
                new SetChargingProfileRequestHandler(brokerContext.getChargingStationRouteResolver(csId))
        );

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
        ICallResult<BootNotificationResponse> response = csApi.getCsmsProxy().sendBootNotificationRequest(bootRequest);


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
