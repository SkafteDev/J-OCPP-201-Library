package dk.sdu.mmmi.anylogic.messaging;

public interface IMessageRouteResolver {
    String getRoute(AnyLogicMessageType msgType);
}
