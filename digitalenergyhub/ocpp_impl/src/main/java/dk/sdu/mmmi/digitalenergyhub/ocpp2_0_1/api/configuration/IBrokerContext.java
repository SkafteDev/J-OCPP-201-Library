package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.configuration;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConfig;

public interface IBrokerContext {

    BrokerConfig getConfigFromCsId(String csId);

    BrokerConfig getConfigFromCsmsId(String csmsId);

    IMessageRouteResolver getCsmsRouteResolver(String csmsId);

    IMessageRouteResolver getChargingStationRouteResolver(String csId);
}
