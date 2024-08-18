package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import io.nats.client.Connection;

public interface OCPPRequestHandler<TRequest, TResponse> {
    void activate();

    void deactivate();

    String serialize(ICallResult<TResponse> message);

    ICall<TRequest> deserialize(String rawJsonPayload);

    /**
     * The method that contains the logic for how to handle the
     * {@link ICall} payload.
     * The method must handle the CALL and provide a CALLRESULT
     * {@link ICallResult}
     *
     * @param message {@link ICall}
     * @param subject The subject that the message was received on.
     * @return {@link ICallResult}
     */
    ICallResult<TResponse> handle(ICall<TRequest> message, String subject);

    /**
     * The subject on which this request handler listen for incoming requests.
     * Must be overridden in subclasses.
     * E.g. operators.<opId>.csms.<csmsId>.cs.<csId>.requests.bootnotification
     * <p>
     * Use the helper class {@link IMessageRouteResolver}.
     *
     * @return
     */
    String getRequestSubject();
}
