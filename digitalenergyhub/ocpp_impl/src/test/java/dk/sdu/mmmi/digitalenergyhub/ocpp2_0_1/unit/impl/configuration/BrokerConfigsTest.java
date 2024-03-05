package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.unit.impl.configuration;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerContext;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerContextLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerContext;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class BrokerConfigsTest {

    private static final String resourceFile = "BrokerContexts/brokerContext.yml";

    private static final String csIdSearchCriteria = "505f0da2-d44a-43bb-bf19-7ed392bc36a7";

    @Test
    void unit_generate_charging_station_routing_map_from_yaml() {
        URL resource = getResource(resourceFile);

        IBrokerContext configs = BrokerContextLoader.fromYAML(resource.getPath());

        IMessageRouteResolver routes = configs.getChargingStationRouteResolver(csIdSearchCriteria);

        String actualRoute = routes.getRoute(OCPPMessageType.BootNotificationRequest);

        String expectedRoute = String.format("operators.%s.csms.%s.cs.%s.requests.%s",
                "ewii",
                "ewiicsms",
                csIdSearchCriteria,
                OCPPMessageType.BootNotificationRequest.getValue().toLowerCase()
        );

        assertEquals(expectedRoute, actualRoute);
    }

    @Test
    void unit_generate_routing_map_from_yaml_for_non_existing_charging_station() {
        URL resource = getResource(resourceFile);

        IBrokerContext configs = BrokerContextLoader.fromYAML(resource.getPath());

        assertThrows(BrokerContext.ChargingStationIdNotFoundException.class,
                () -> configs.getChargingStationRouteResolver("non-existent charging station id"));

    }

    @Test
    void unit_generate_csms_routing_map_from_yaml() {
        URL resource = getResource(resourceFile);

        IBrokerContext configs = BrokerContextLoader.fromYAML(resource.getPath());

        IMessageRouteResolver routes = configs.getCsmsRouteResolver("Clever CSMS");

        String actualRoute = routes.getRoute(OCPPMessageType.BootNotificationRequest);

        String expectedRoute = String.format("operators.%s.csms.%s.cs.%s.requests.%s",
                "clever",
                "clevercsms",
                "*",
                OCPPMessageType.BootNotificationRequest.getValue().toLowerCase()
        );

        assertEquals(expectedRoute, actualRoute);
    }

    @Test
    void unit_generate_routing_map_from_yaml_for_non_existing_charging_station_management_system() {
        URL resource = getResource(resourceFile);

        IBrokerContext configs = BrokerContextLoader.fromYAML(resource.getPath());

        assertThrows(BrokerContext.ChargingStationManagementSystemIdNotFoundException.class,
                () -> configs.getCsmsRouteResolver("non-existent charging station id"));

    }

    private static URL getResource(String resourcePath) {
        ClassLoader classLoader = BrokerConfigsTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceFile);

        if (resourceUrl == null) {
            fail(String.format("Could not read input resource file: %s. Ensure that the file exists.", resourceFile));
        }

        return resourceUrl;
    }
}