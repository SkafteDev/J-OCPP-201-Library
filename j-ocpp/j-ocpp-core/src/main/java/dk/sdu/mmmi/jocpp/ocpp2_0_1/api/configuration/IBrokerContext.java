package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerConfig;

import java.util.Collection;

public interface IBrokerContext {

    BrokerConfig getConfigFromCsId(String csId);

    BrokerConfig getConfigFromCsmsId(String csmsId);

    Collection<String> getCsmsIds();

    IMessageRouteResolver getCsmsRouteResolver(String csmsId);

    IMessageRouteResolver getChargingStationRouteResolver(String csId);
}
