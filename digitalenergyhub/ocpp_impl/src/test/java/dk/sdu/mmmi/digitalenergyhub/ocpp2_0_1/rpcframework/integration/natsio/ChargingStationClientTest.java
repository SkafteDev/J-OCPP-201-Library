package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.integration.natsio;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRoutingMapImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationClientNatsIo;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.ChargingStationManagementServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChargingStationClientTest {

    private static final String NATS_CONNECTION_STRING = "nats://localhost:4222";
    private static final String OPERATOR_ID = "Clever";
    private static final String CSMS_ID = "Csms1";
    private static final String CS_ID = "DENMARK_ODENSE_M_DRAEJEBAENKEN_CS_1";

    private static ChargingStationManagementServerImpl csmsImpl;

    @BeforeAll
    static void setup_csms() {
        // Set up a new management system that can respond to incoming messages.
        csmsImpl = new ChargingStationManagementServerImpl(OPERATOR_ID, CSMS_ID,
                NATS_CONNECTION_STRING);
        csmsImpl.connect();
        csmsImpl.serve();
    }

    @Test
    void integration_cs_to_csms_BootNotificationRequest() {
        Connection natsConnection = null;
        try {
            natsConnection = Nats.connect(Options.builder()
                    .server(NATS_CONNECTION_STRING)
                    .connectionName(CS_ID)
                    .build());
        } catch (IOException | InterruptedException cause) {
            fail("Failed to connect to a NATS.IO server", cause);
        }

        IMessageRoutingMap routingMap = new MessageRoutingMapImpl(OPERATOR_ID, CSMS_ID, CS_ID);

        IChargingStationClientApi csClient = new ChargingStationClientNatsIo(natsConnection, routingMap);

        ICallMessage<BootNotificationRequest> bootNotificationRequest = createBootNotificationRequest();

        ICallResultMessage<BootNotificationResponse> bootNotificationResponse =
                csClient.sendBootNotificationRequest(bootNotificationRequest);

        if (bootNotificationResponse == null) {
            fail("Received 'null' response.");
        }

        // MessageId's of request/response must be equal for tracability.
        assertEquals(bootNotificationRequest.getMessageId(), bootNotificationResponse.getMessageId());
        // Expect a call result.
        assertEquals(MessageType.CALLRESULT, bootNotificationResponse.getMessageTypeId());
        // Expect that the boot is accepted by default.
        assertEquals(RegistrationStatusEnum.ACCEPTED, bootNotificationResponse.getPayload().getStatus());
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
