/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

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
