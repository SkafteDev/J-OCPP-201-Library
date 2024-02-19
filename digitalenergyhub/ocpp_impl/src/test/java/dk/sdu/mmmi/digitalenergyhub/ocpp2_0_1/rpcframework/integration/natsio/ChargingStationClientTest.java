package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.integration.natsio;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning.IChargingStationProvisioningClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.MessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationProvisioningClientImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootNotificationResponse;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.BootReasonEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class ChargingStationClientTest {

    private static final String NATS_CONNECTION_STRING = "nats://localhost:4222";
    private static final String OPERATOR_ID = "Clever";
    private static final String CSMS_ID = "Csms1";
    private static final String CS_ID = "DENMARK_ODENSE_M_DRAEJEBAENKEN_CS_1";

    @Test
    void integration_cs_to_csms_BootNotificationRequest() {
        Connection natsConnection = null;
        try {
            natsConnection = Nats.connect(NATS_CONNECTION_STRING);
        } catch (IOException | InterruptedException cause) {
            fail("Failed to connect to a NATS.IO server", cause);
        }

        MessageRoutingMap routingMap = new MessageRoutingMap(OPERATOR_ID, CSMS_ID, CS_ID);

        IChargingStationProvisioningClientApi csms = new ChargingStationProvisioningClientImpl(natsConnection, routingMap);

        ICallMessage<BootNotificationRequest> bootNotificationRequest = createBootNotificationRequest();

        ICallResultMessage<BootNotificationResponse> bootNotificationResponse =
                csms.sendBootNotificationRequest(bootNotificationRequest);

        if (bootNotificationResponse == null) {
            fail("Received 'null' response.");
        }
    }

    private static ICallMessage<BootNotificationRequest> createBootNotificationRequest() {
        BootNotificationRequest bootNotificationRequest = new BootNotificationRequest.BootNotificationRequestBuilder()
                .withReason(BootReasonEnum.POWER_UP)
                .withChargingStation(
                        new ChargingStation.ChargingStationBuilder()
                                .withModel("SingleSocketCharger")
                                .withVendorName("VendorX")
                                .build())
                .build();

        ICallMessage<BootNotificationRequest> callMessage = CallMessageImpl.<BootNotificationRequest>newBuilder()
                .withMessageId("82854779-f4d2-49c8-9c3c-437ce47e523c")
                .asAction("BootNotification")
                .withPayLoad(bootNotificationRequest)
                .build();

        return callMessage;
    }
}
