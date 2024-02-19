package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.chargingstation.provisioning;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.MessageRoutingMap;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;

/**
 * This interface is part of the functional block B. Provisioning.
 *
 * This interface is intended to be used by a Charging Station to
 * interoperate with a Charging Station Management System through request/response messages.
 */
public interface IChargingStationProvisioningClientApi {
    ICallResultMessage<BootNotificationResponse> sendBootNotificationRequest(ICallMessage<BootNotificationRequest> req);
    ICallResultMessage<StatusNotificationResponse> sendStatusNotificationRequest(ICallMessage<StatusNotificationRequest> req);
    ICallResultMessage<HeartbeatResponse> sendHeartbeatRequest(ICallMessage<HeartbeatRequest> req);
    ICallResultMessage<NotifyReportResponse> sendNotifyReportRequest(ICallMessage<NotifyReportRequest> req);
    MessageRoutingMap getMessageRoutingMap();
}
