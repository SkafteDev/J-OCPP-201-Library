package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.functionalblocks.provisioning;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;

/**
 * This interface is part of the functional block B. Provisioning.
 * This interface must be implemented by the Charging Station.
 */
public interface ICSProvisioning {
    GetVariablesResponse sendGetVariablesRequest(GetVariablesRequest req);

    SetVariablesResponse sendSetVariablesRequest(SetVariablesRequest req);

    GetBaseReportResponse sendGetBaseReportRequest(GetBaseReportRequest req);

    GetReportResponse sendGetReportRequest(GetReportRequest req);

    SetNetworkProfileResponse sendSetNetworkProfileRequest(SetNetworkProfileRequest req);

    ResetResponse sendResetRequest(ResetRequest req);


}
