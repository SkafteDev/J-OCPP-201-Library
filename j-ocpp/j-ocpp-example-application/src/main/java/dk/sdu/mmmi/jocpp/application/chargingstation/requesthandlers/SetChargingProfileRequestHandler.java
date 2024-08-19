package dk.sdu.mmmi.jocpp.application.chargingstation.requesthandlers;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling.OCPPOverNatsIORequestHandler;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.ChargingProfileStatusEnum;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.SetChargingProfileResponse;
import io.nats.client.Connection;

import java.util.logging.Logger;

public class SetChargingProfileRequestHandler
        extends OCPPOverNatsIORequestHandler<SetChargingProfileRequest, SetChargingProfileResponse> {

    private final Logger logger = Logger.getLogger(SetChargingProfileRequestHandler.class.getName());

    private final IMessageRouteResolver routeResolver;

    public SetChargingProfileRequestHandler(IMessageRouteResolver routeResolver, Connection natsConnection) {
        super(SetChargingProfileRequest.class, SetChargingProfileResponse.class, natsConnection);
        this.routeResolver = routeResolver;
    }

    @Override
    public ICallResult<SetChargingProfileResponse> handle(ICall<SetChargingProfileRequest> callMessage, String subject) {
        // TODO: Update the internal state of the CS
        SetChargingProfileRequest requestPayload = callMessage.getPayload();
        logger.info("Updated internal state...");

        // TODO: Create response depending on the internal state...
        SetChargingProfileResponse responsePayload = SetChargingProfileResponse.builder()
                .withStatus(ChargingProfileStatusEnum.ACCEPTED)
                .build();

        ICallResult<SetChargingProfileResponse> callResult =
                CallResultImpl.<SetChargingProfileResponse>newBuilder()
                        .withMessageId(callMessage.getMessageId()) // MessageId MUST be identical to the call.
                        .withPayLoad(responsePayload)
                        .build();

        return callResult;
    }

    @Override
    public String getRequestSubject() {
        return routeResolver.getRoute(OCPPMessageType.SetChargingProfileRequest);
    }
}
