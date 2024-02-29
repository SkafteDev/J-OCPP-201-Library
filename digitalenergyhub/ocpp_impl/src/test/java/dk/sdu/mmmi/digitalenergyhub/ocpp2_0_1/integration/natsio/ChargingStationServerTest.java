package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.integration.natsio;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.managementsystem.ICsmsClientApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation.IOCPPServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.managementsystem.CsmsClientImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfigsLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerConnectorConfigs;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.chargingstation.ChargingStationServerImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.SetChargingProfileRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class ChargingStationServerTest {
    private static final String CS_ID = "ce2b8b0e-db26-4643-a705-c848fab64327";

    private IOCPPServer csServerImpl;

    @BeforeEach
    void setup_chargingstation_and_connect_to_nats() {
        URL resource = getResource("RoutingConfigs/brokerConnectorConfigs.yml");
        IBrokerConnectorConfigs brokerConnectorLookup = BrokerConnectorConfigsLoader.fromYAML(resource.getPath());
        String brokerUrl = brokerConnectorLookup.getConfigFromCsId(CS_ID).getBrokerUrl();
        Connection natsConnection = getNatsConnection(brokerUrl);
        IMessageRouteResolver routeResolver = brokerConnectorLookup.getChargingStationRouteResolver(CS_ID);

        csServerImpl = new ChargingStationServerImpl(natsConnection, routeResolver);
        csServerImpl.addRequestHandler(OCPPMessageType.SetChargingProfileRequest,
                new SetChargingProfileRequestHandler(csServerImpl.getMsgRouteResolver()));
    }

    private static URL getResource(String resourceFile) {
        ClassLoader classLoader = ChargingStationServerTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceFile);

        if (resourceUrl == null) {
            fail(String.format("Could not read input resource file: %s. Ensure that the file exists.", resourceFile));
        }

        return resourceUrl;
    }

    private static Connection getNatsConnection(String natsConnectionString) {
        Options natsOptions = Options.builder()
                .server(natsConnectionString)
                .connectionTimeout(Duration.ofMinutes(2))
                .build();

        try {
            return Nats.connect(natsOptions);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void integration_ChargingStationServer_handle_SetChargingProfileRequest() {
        URL resource = getResource("RoutingConfigs/brokerConnectorConfigs.yml");
        IBrokerConnectorConfigs brokerConnectorLookup = BrokerConnectorConfigsLoader.fromYAML(resource.getPath());
        IMessageRouteResolver routeResolver = brokerConnectorLookup.getChargingStationRouteResolver(CS_ID);
        String brokerUrl = brokerConnectorLookup.getConfigFromCsId(CS_ID).getBrokerUrl();

        ICsmsClientApi csmsClientApi = new CsmsClientImpl(getNatsConnection(brokerUrl), routeResolver);

        ChargingProfile chargingProfile = getChargingProfile();

        SetChargingProfileRequest payload = SetChargingProfileRequest.builder()
                .withEvseId(0)
                .withChargingProfile(chargingProfile)
                .build();

        ICallMessage<SetChargingProfileRequest> call = CallMessageImpl.<SetChargingProfileRequest>newBuilder()
                .asAction(OCPPMessageType.SetChargingProfileRequest.getValue())
                .withMessageId("xxxyyyzzz123")
                .withPayLoad(payload)
                .build();


        ICallResultMessage<SetChargingProfileResponse> callResultMessage = csmsClientApi.sendSetChargingProfileRequest(call);

        System.out.println(callResultMessage);
    }

    private static ChargingProfile getChargingProfile() {
        return ChargingProfile.builder()
                .withId(1)
                .withStackLevel(0)
                .withChargingProfilePurpose(ChargingProfilePurposeEnum.CHARGING_STATION_MAX_PROFILE)
                .withChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE)
                .withChargingSchedule(
                        List.of(
                                ChargingSchedule.builder()
                                        .withId(1)
                                        .withStartSchedule(DateUtil.of(2024, 1, 20, 0, 0, 0))
                                        .withChargingRateUnit(ChargingRateUnitEnum.W)
                                        /*
                                         * From this point is the limit for each hour in the day
                                         */
                                        .withChargingSchedulePeriod(List.of(
                                                // From 00:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(0 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 01:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(1 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 02:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(2 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 03:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(3 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 04:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(4 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 05:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(5 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 06:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(6 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 07:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(7 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 08:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(8 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 09:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(9 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 10:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(10 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 11:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(11 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 12:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(12 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 13:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(13 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 14:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(14 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 15:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(15 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 16:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(16 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 17:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(17 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 18:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(18 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 19:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(19 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 20:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(20 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 21:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(21 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 22:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(22 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 23:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(23 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build()
                                        )).build()
                        ))
                .build();
    }
}
