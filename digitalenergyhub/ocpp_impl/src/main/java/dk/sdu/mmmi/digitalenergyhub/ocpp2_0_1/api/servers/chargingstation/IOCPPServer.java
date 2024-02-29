package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.dispatching.OCPPRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileRequest;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.SetChargingProfileResponse;

/**
 * This interface represents a server that can manage dispatchers.
 */
public interface IOCPPServer {

    <IN extends ICallMessage<?>, OUT extends ICallResultMessage<?>> void addRequestHandler(OCPPMessageType msgType, OCPPRequestHandler<SetChargingProfileRequest, SetChargingProfileResponse> requestHandler);

    IMessageRouteResolver getMsgRouteResolver();
}
