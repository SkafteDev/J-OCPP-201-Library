package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.functionalblocks.provisioning;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import dk.sdu.mmmi.protobuf.ocpp2_0_1.messages.StatusNotificationResponse;

/**
 * This interface is part of the functional block B. Provisioning.
 * This interface must be implemented by the Charging Station Management System.
 */
public interface ICSMSProvisioning {
    BootNotificationResponse sendBootNotificationRequest(BootNotificationRequest req);
    StatusNotificationResponse sendStatusNotificationRequest(StatusNotificationRequest req);
    HeartbeatResponse sendHeartbeatRequest(HeartbeatRequest req);
    NotifyReportResponse sendNotifyReportRequest(NotifyReportRequest req);

}
