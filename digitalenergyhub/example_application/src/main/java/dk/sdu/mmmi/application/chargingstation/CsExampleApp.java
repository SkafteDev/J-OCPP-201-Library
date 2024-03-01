package dk.sdu.mmmi.application.chargingstation;

import dk.sdu.mmmi.application.chargingstation.requesthandlers.SetChargingProfileRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfigsLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerConnectorConfigs;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;

import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class CsExampleApp {

    private static final String quitToken = "q";

    public static void main(String[] args) {
        String csId = "ce2b8b0e-db26-4643-a705-c848fab64327"; // This has to be hard coded or configured somewhere...

        URL resource = ClassLoader.getSystemResource("brokerConnectorConfigs.yml");

        IBrokerConnectorConfigs brokerConnectorConfigs = BrokerConnectorConfigsLoader.fromYAML(resource.getPath());

        ChargingStationApi csApi = ChargingStationApi.newBuilder()
                .withBrokerConnectorConfigs(brokerConnectorConfigs)
                .withCsId(csId)
                .build();

        csApi.getChargingStationServer().addRequestHandler(
                OCPPMessageType.SetChargingProfileRequest,
                new SetChargingProfileRequestHandler(brokerConnectorConfigs.getChargingStationRouteResolver(csId))
        );

        BootNotificationRequest bootNotificationRequest = BootNotificationRequest.builder()
                .withChargingStation(ChargingStation.builder()
                        .withVendorName("ABB")
                        .withSerialNumber("47888eec-b9e5-4d67-9f36-136126e158c8")
                        .withModel("ABB TAC-W11-G5-R-0")
                        .withFirmwareVersion("3.1.3")
                        .build())
                .withReason(BootReasonEnum.POWER_UP)
                .build();

        ICallMessage<BootNotificationRequest> bootRequest = CallMessageImpl.<BootNotificationRequest>newBuilder()
                .withMessageId(UUID.randomUUID().toString())
                .asAction(OCPPMessageType.BootNotificationRequest.getValue())
                .withPayLoad(bootNotificationRequest)
                .build();

        ICallResultMessage<BootNotificationResponse> response = csApi.getCsmsProxy().sendBootNotificationRequest(bootRequest);
        System.out.println(response.getPayload().toString());


        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }
}
