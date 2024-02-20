package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;

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
public interface IMessageRoutingMap {
    String getRoute(OCPPMessageType msgType);
}
