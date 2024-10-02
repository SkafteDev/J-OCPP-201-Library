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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;

/**
 * This interface represents a registry that manages request handlers.
 */
public interface IRequestHandlerRegistry {

    <TRequest, TResponse> void addRequestHandler(OCPPMessageType msgType, OCPPRequestHandler<TRequest, TResponse> requestHandler);

    void removeRequestHandler(OCPPMessageType msgType);

    boolean existsRequestHandler(OCPPMessageType msgType);

    IMessageRouteResolver getMsgRouteResolver();
}
