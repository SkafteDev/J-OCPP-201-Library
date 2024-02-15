package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.provisioning;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;

/**
 * This interface is part of the functional block B. Provisioning.
 *
 * This interface is intended to be used by a Charging Station Management System to
 * interoperate with a Charging Station through request/response messages.
 */
public interface IChargingStationProvisioningApi {
    ICallResultMessage<GetVariablesResponse> sendGetVariablesRequest(ICallMessage<GetVariablesRequest> req);

    ICallResultMessage<SetVariablesResponse> sendSetVariablesRequest(ICallMessage<SetVariablesRequest> req);

    ICallResultMessage<GetBaseReportResponse> sendGetBaseReportRequest(ICallMessage<GetBaseReportRequest> req);

    ICallResultMessage<GetReportResponse> sendGetReportRequest(ICallMessage<GetReportRequest> req);

    ICallResultMessage<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICallMessage<SetNetworkProfileRequest> req);

    ICallResultMessage<ResetResponse> sendResetRequest(ICallMessage<ResetRequest> req);


}
