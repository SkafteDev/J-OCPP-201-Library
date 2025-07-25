
package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.requesthandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.deserializers.CallDeserializer;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.serializers.CallResultSerializer;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

public abstract class OCPPOverNatsIORequestHandler<TRequest, TResponse> implements OCPPRequestHandler<TRequest, TResponse> {
    private static final Logger logger = LoggerFactory.getLogger(OCPPOverNatsIORequestHandler.class.getName());

    // Store the payload type for the inbound request. Needed for serialization.
    private final Class<TRequest> inboundPayloadType;

    // Store the payload type for the outbound request. Needed for deserialization.
    private final Class<TResponse> outboundPayloadType;

    // The underlying dispatcher. In this case io.nats.client.Dispatcher.
    private Dispatcher dispatcher;
    private Connection natsConnection;

    /**
     * Instantiate a new OCPPRequestHandler with the INBOUND and OUTBOUND payload types.
     * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
     * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
     */
    public OCPPOverNatsIORequestHandler(Class<TRequest> inPayloadType, Class<TResponse> outPayloadType, Connection natsConnection) {
        this.inboundPayloadType = inPayloadType;
        this.outboundPayloadType = outPayloadType;
        this.natsConnection = natsConnection;
    }

    /**
     * Registers this request handler on the provided Connection.
     * The handler will start serving requests on the provided connection.
     * @see Connection
     */
    @Override
    public void activate() {
        if (dispatcher != null) return;

        this.dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received request payload='%s' on subject '%s'",
                    jsonPayload,
                    natsMsg.getSubject()));

            ICall<TRequest> callMessage = deserialize(jsonPayload);

            // Handle the message internally, e.g. update state, store in db, etc.
            ICallResult<TResponse> callResult = handle(callMessage, natsMsg.getSubject());

            String jsonResponsePayload = serialize(callResult);

            Message natsResponse = NatsMessage.builder()
                    .subject(natsMsg.getReplyTo()) // Reply subject must use the call's provided reply subject.
                    .data(jsonResponsePayload)
                    .build();

            // Publish the response on the subject that the requester is listening on.
            natsConnection.publish(natsResponse);

            // Publish the response on the subject used for traceability.
            String replyTo = natsMsg.getHeaders().getFirst("replyTo");
            if (!replyTo.equals(null) || !replyTo.isEmpty()){
                NatsMessage duplicateResponse = NatsMessage.builder()
                        .subject(replyTo)
                        .data(jsonResponsePayload)
                        .build();
                natsConnection.publish(duplicateResponse);
            }

        });
        dispatcher.subscribe(getRequestSubject());
    }

    /**
     * Unregister the request handler and stop serving requests.
     */
    @Override
    public void deactivate() {
        dispatcher.unsubscribe(getRequestSubject());
    }

    /**
     * Serializes the {@link ICallResult} to JSON.
     * @param message The {@link ICallResult} to serialize.
     * @return The serialized message in JSON.
     */
    @Override
    public String serialize(ICallResult<TResponse> message) {
        try {
            return CallResultSerializer.serialize(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Deserializes the raw payload in Json.
     * @param rawJsonPayload
     * @return
     */
    @Override
    public ICall<TRequest> deserialize(String rawJsonPayload) {
        ICall<TRequest> deserialized;

        try {
            deserialized = CallDeserializer.deserialize(rawJsonPayload, inboundPayloadType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return deserialized;
    }


}
