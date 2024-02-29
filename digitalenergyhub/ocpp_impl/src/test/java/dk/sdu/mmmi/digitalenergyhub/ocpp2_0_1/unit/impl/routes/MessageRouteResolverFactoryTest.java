package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.unit.impl.routes;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRouteResolverFactory;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class MessageRouteResolverFactoryTest {

    private static final String resourceFile = "RoutingConfigs/routingConfigExample.yml";

    private static final String csIdSearchCriteria = "505f0da2-d44a-43bb-bf19-7ed392bc36a7";

    @Test
    void unit_generate_charging_station_routing_map_from_yaml() {
        URL resource = getResource(resourceFile);

        IMessageRouteResolver routes = MessageRouteResolverFactory.chargingStationRoutesFromYaml(resource.getPath(), csIdSearchCriteria);

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

        assertThrows(MessageRouteResolverFactory.ChargingStationIdNotFoundException.class,
                () -> MessageRouteResolverFactory.chargingStationRoutesFromYaml(resource.getPath(), "non-existent charging station id"));

    }

    @Test
    void unit_generate_csms_routing_map_from_yaml() {
        URL resource = getResource(resourceFile);

        IMessageRouteResolver routes = MessageRouteResolverFactory.csmsRoutesFromYaml(resource.getPath(), "Clever CSMS");

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

        assertThrows(MessageRouteResolverFactory.ChargingStationManagementSystemIdNotFoundException.class,
                () -> MessageRouteResolverFactory.csmsRoutesFromYaml(resource.getPath(), "non-existent charging station id"));

    }

    private static URL getResource(String resourcePath) {
        ClassLoader classLoader = MessageRouteResolverFactoryTest.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceFile);

        if (resourceUrl == null) {
            fail(String.format("Could not read input resource file: %s. Ensure that the file exists.", resourceFile));
        }

        return resourceUrl;
    }
}