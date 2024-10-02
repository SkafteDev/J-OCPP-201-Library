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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;

/**
 * This interface contains the operations to get delivery routes for individual OCPP 2.0.1 Message types as defined in:
 * OCPP 2.0.1: Part 2 - Specification.
 *
 * Example message types:
 * AuthorizeRequest
 * AuthorizeResponse
 * BootNotificationRequest
 * BootNotificationResponse
 * etc.
 */
public interface IMessageRouteResolver {
    String getRoute(OCPPMessageType msgType);

    String getRequestRoute();

    String getResponseRoute();

    String getConnectRoute();

    String getCsIdentity();

    String getCsmsIdentity();
}
