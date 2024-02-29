package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.integration.natsio;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.IChargingStationClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation.ChargingStationClientNatsIo;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfig;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfigsLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfigs;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRouteResolverImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.ChargingStationManagementServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.MessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChargingStationClientTest {
    private static final String CS_ID = "ce2b8b0e-db26-4643-a705-c848fab64327";

    private static final String CSMS_ID = "Clever CSMS";

    private ChargingStationManagementServerImpl csmsImpl;

    @BeforeEach
    void setup_and_connect_csms_to_nats() {
        URL resource = getResource("RoutingConfigs/brokerConnectorConfigs.yml");
        BrokerConnectorConfigs brokerConnectorConfigs = BrokerConnectorConfigsLoader.loadBrokerConnectionConfigs(resource.getPath());
        BrokerConnectorConfig csmsConnectorConfig = brokerConnectorConfigs.getConfigFromCsmsId(CSMS_ID);

        /*
         * Set up a new management system that can respond to incoming messages.
         */
        Connection natsConnection = getNatsConnection(csmsConnectorConfig.getBrokerUrl());
        csmsImpl = new ChargingStationManagementServerImpl(
                csmsConnectorConfig.getOperatorId(),
                csmsConnectorConfig.getCsmsId(),
                natsConnection);

        csmsImpl.connect();
        csmsImpl.serve();
    }

    private static URL getResource(String resourceFile) {
        ClassLoader classLoader = ChargingStationServerTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceFile);

        if (resourceUrl == null) {
            fail(String.format("Could not read input resource file: %s. Ensure that the file exists.", resourceFile));
        }

        return resourceUrl;
    }

    @Test
    void integration_cs_to_csms_BootNotificationRequest() {
        URL resource = getResource("RoutingConfigs/brokerConnectorConfigs.yml");
        BrokerConnectorConfigs brokerConnectorConfigs = BrokerConnectorConfigsLoader.loadBrokerConnectionConfigs(resource.getPath());
        BrokerConnectorConfig csConnectorConfig = brokerConnectorConfigs.getConfigFromCsId(CS_ID);

        Connection natsConnection = getNatsConnection(csConnectorConfig.getBrokerUrl());

        IMessageRouteResolver routeResolver = brokerConnectorConfigs.getChargingStationRouteResolver(CS_ID);

        IChargingStationClientApi csClient = new ChargingStationClientNatsIo(natsConnection, routeResolver);

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

    private static Connection getNatsConnection(String natsUrl) {
        Connection natsConnection = null;
        try {
            natsConnection = Nats.connect(Options.builder()
                    .server(natsUrl)
                    .build());
        } catch (IOException | InterruptedException cause) {
            fail("Failed to connect to a NATS.IO server", cause);
        }
        return natsConnection;
    }

    @Test
    void integration_cs_to_csms_StatusNotificationRequest() {
        URL resource = getResource("RoutingConfigs/brokerConnectorConfigs.yml");
        BrokerConnectorConfigs brokerConnectorConfigs = BrokerConnectorConfigsLoader.loadBrokerConnectionConfigs(resource.getPath());
        BrokerConnectorConfig csConnectorConfig = brokerConnectorConfigs.getConfigFromCsId(CS_ID);

        Connection natsConnection = getNatsConnection(csConnectorConfig.getBrokerUrl());

        IMessageRouteResolver routeResolver = brokerConnectorConfigs.getChargingStationRouteResolver(CS_ID);

        IChargingStationClientApi csClient = new ChargingStationClientNatsIo(natsConnection, routeResolver);

        ICallMessage<StatusNotificationRequest> statusNotificationRequest = createStatusNotificationRequest();

        ICallResultMessage<StatusNotificationResponse> statusNotificationResponse =
                csClient.sendStatusNotificationRequest(statusNotificationRequest);

        if (statusNotificationResponse == null) {
            fail("Received 'null' response.");
        }

        // MessageId's of request/response must be equal for tracability.
        assertEquals(statusNotificationRequest.getMessageId(), statusNotificationResponse.getMessageId());
        // Expect a call result.
        assertEquals(MessageType.CALLRESULT, statusNotificationResponse.getMessageTypeId());
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
                .asAction(OCPPMessageType.BootNotificationRequest.getValue())
                .withPayLoad(bootNotificationRequest)
                .build();

        return callMessage;
    }

    private static ICallMessage<StatusNotificationRequest> createStatusNotificationRequest() {
        StatusNotificationRequest statusNotificationRequest = new StatusNotificationRequest.StatusNotificationRequestBuilder()
                .withEvseId(0)
                .withConnectorId(0)
                .withTimestamp(DateUtil.now())
                .withConnectorStatus(ConnectorStatusEnum.OCCUPIED)
                .build();

        ICallMessage<StatusNotificationRequest> callMessage = CallMessageImpl.<StatusNotificationRequest>newBuilder()
                .withMessageId("53d9c081-aa03-4456-a5f4-811fc870f0bd")
                .asAction(OCPPMessageType.StatusNotificationRequest.getValue())
                .withPayLoad(statusNotificationRequest)
                .build();

        return callMessage;
    }
}
