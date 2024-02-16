package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.clients.provisioning.IChargingStationProvisioningApi;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import io.nats.client.Connection;

public class ChargingStationProvisioningImpl implements IChargingStationProvisioningApi {

    private final Connection natsConnection;

    public ChargingStationProvisioningImpl(Connection natsConnection) {
        this.natsConnection = natsConnection;
    }

    @Override
    public ICallResultMessage<GetVariablesResponse> sendGetVariablesRequest(ICallMessage<GetVariablesRequest> req) {
        // ICallMessage<GetVariablesRequest> callMsg = CallMessageImpl.<GetVariablesRequest>newBuilder().build();

        // Starting here:

        // Serialize to JSON format.
        // Optionally, validate format before sending.

        // Send the msg to the NATS.io broker as a request with a pre-programmed timeout.

        // Await a response from NATS.io.

        // Deserialize the response from NATS.io.

        // Return the response...
        return null;
    }

    @Override
    public ICallResultMessage<SetVariablesResponse> sendSetVariablesRequest(ICallMessage<SetVariablesRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<GetBaseReportResponse> sendGetBaseReportRequest(ICallMessage<GetBaseReportRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<GetReportResponse> sendGetReportRequest(ICallMessage<GetReportRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<SetNetworkProfileResponse> sendSetNetworkProfileRequest(ICallMessage<SetNetworkProfileRequest> req) {
        return null;
    }

    @Override
    public ICallResultMessage<ResetResponse> sendResetRequest(ICallMessage<ResetRequest> req) {
        return null;
    }
}
