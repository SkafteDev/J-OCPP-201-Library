package dk.sdu.mmmi.application.chargingstation.requesthandlers;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.OCPPRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallResultMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfileStatusEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileResponse;

import java.util.logging.Logger;

public class SetChargingProfileRequestHandler
        extends OCPPRequestHandler<SetChargingProfileRequest, SetChargingProfileResponse> {

    private final Logger logger = Logger.getLogger(SetChargingProfileRequestHandler.class.getName());

    private final IMessageRouteResolver routingMap;

    public SetChargingProfileRequestHandler(IMessageRouteResolver routingMap) {
        super(SetChargingProfileRequest.class, SetChargingProfileResponse.class);
        this.routingMap = routingMap;
    }

    @Override
    public ICallResultMessage<SetChargingProfileResponse> handle(ICallMessage<SetChargingProfileRequest> callMessage, String subject) {
        // Update the internal state
        SetChargingProfileRequest requestPayload = callMessage.getPayload();
        //this.chargingStationDeviceModel.setChargingProfile(requestPayload.getChargingProfile());
        logger.info("Updated internal state...");

        // TODO: Create response depending on the internal state...
        SetChargingProfileResponse responsePayload = SetChargingProfileResponse.builder()
                .withStatus(ChargingProfileStatusEnum.ACCEPTED)
                .build();

        ICallResultMessage<SetChargingProfileResponse> callResult =
                CallResultMessageImpl.<SetChargingProfileResponse>newBuilder()
                        .withMessageId(callMessage.getMessageId()) // MessageId MUST be identical to the call.
                        .withPayLoad(responsePayload)
                        .build();

        return callResult;
    }

    @Override
    public String getRequestSubject() {
        return routingMap.getRoute(OCPPMessageType.SetChargingProfileRequest);
    }
}
