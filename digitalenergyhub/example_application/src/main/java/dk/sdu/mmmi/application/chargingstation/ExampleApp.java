package dk.sdu.mmmi.application.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConnectorConfigsLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerConnectorConfigs;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.NatsRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileResponse;

import java.net.URL;

public class ExampleApp {
    public static void main(String[] args) {
        URL resource = ExampleApp.class.getResource("connectorConfigs.yml");

        IBrokerConnectorConfigs brokerConnectorConfigs = BrokerConnectorConfigsLoader.fromYAML(resource.getPath());


        ChargingStationConnector connector = ChargingStationConnector.newBuilder()
                .withBrokerConnectorConfigs(brokerConnectorConfigs)
                .withCsId("ce2b8b0e-db26-4643-a705-c848fab64327")
                .build();

        connector.getChargingStationServer().addRequestHandler(
                OCPPMessageType.SetChargingProfileRequest,
                new NatsRequestHandler<ICallMessage<SetChargingProfileRequest>, ICallResultMessage<SetChargingProfileResponse>>(
                        SetChargingProfileRequest.class,
                        SetChargingProfileResponse.class
                ) {
                    @Override
                    public void process(ICallMessage<SetChargingProfileRequest> message) {

                    }

                    @Override
                    public ICallResultMessage<SetChargingProfileResponse> generateResponse(ICallMessage<SetChargingProfileRequest> message) {
                        return null;
                    }

                    @Override
                    public String getInboundMessageRoute() {
                        return null;
                    }
                }
        );
    }
}
