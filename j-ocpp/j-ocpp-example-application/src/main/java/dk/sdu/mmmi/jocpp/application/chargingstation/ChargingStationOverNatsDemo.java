package dk.sdu.mmmi.jocpp.application.chargingstation;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.clients.chargingstation.ChargingStationNatsClient;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerContextLoader;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
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
         * (1) Instantiate the API to communicate between CS <-> CSMS.
         */
        String csId = "f8125503-8d0f-467f-abad-b830ca6782e2"; // This has to be hard coded or configured directly on the charging station's firmware...
        URL resource = ClassLoader.getSystemResource("brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());

        // Build and connect the CS client to the CSMS before sending requests.
        IOCPPSession ocppSession = ChargingStationNatsClient.newBuilder()
                .withBrokerContext(brokerContext)
                .withCsId(csId)
                .withCsServiceInterface(new CSImpl())
                .build();

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
        Headers headers = Headers.emptyHeader();
        ICallResult<BootNotificationResponse> response = ocppSession.getCsms().sendBootNotificationRequest(headers, bootRequest);

        /*
         * (6) Send another request from CS -> CSMS.
         */
        ICall<ClearedChargingLimitRequest> call = CallImpl.<ClearedChargingLimitRequest>newBuilder()
                .asAction(OCPPMessageType.ClearedChargingLimitRequest.getAction())
                .withMessageId(UUID.randomUUID().toString())
                .withPayLoad(ClearedChargingLimitRequest.builder()
                        .withEvseId(0)
                        .withChargingLimitSource(ChargingLimitSourceEnum.EMS)
                        .build())
                .build();
        ICallResult<ClearedChargingLimitResponse> result = ocppSession.getCsms().sendClearedChargingLimitRequest(Headers.emptyHeader(), call);
        System.out.println(result);


        /*
         * (7) Prevent the program for exiting to simulate that the CS is now awaiting requests from the CSMS.
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
