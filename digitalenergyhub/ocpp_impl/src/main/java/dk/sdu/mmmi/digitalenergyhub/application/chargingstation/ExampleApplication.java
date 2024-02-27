package dk.sdu.mmmi.digitalenergyhub.application.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 *
 */
public class ExampleApplication {
    private static final String quitToken = "q";

    public static void main(String[] args) {
        ChargingStationFacade csFacade = ChargingStationFacade.newBuilder()
                .withOperatorId("Clever")
                .withCsmsId("Clever Central CSMS")
                .withCsId("DENMARK_ODENSE_M_DRAEJEBAENKEN_CS_1")
                .withSerialNumber("47888eec-b9e5-4d67-9f36-136126e158c8")
                .withVendorName("ABB")
                .withModel("ABB TAC-W11-G5-R-0")
                .withFirmwareVersion("3.1.3")
                .withNatsConnectionUrl("nats://localhost:4222")
                .build();

        BootNotificationRequest bootNotificationRequest = BootNotificationRequest.builder()
                .withChargingStation(csFacade.getCsDeviceModel().getChargingStationInfo())
                .build();

        ICallMessage<BootNotificationRequest> bootRequest = CallMessageImpl.<BootNotificationRequest>newBuilder()
                .withMessageId(UUID.randomUUID().toString())
                .asAction(OCPPMessageType.BootNotificationRequest.getValue())
                .withPayLoad(bootNotificationRequest)
                .build();

        ICallResultMessage<BootNotificationResponse> bootNotificationResponseICallResultMessage = csFacade.getChargingStationApi().sendBootNotificationRequest(bootRequest);

        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }
}
