package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallResultMessageSerializer;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public abstract class OCPPRequestHandler<INBOUND, OUTBOUND> {
    private final Logger logger = Logger.getLogger(OCPPRequestHandler.class.getName());

    // Store the payload type for the inbound request. Needed for serialization.
    private final Class<INBOUND> inboundPayloadType;

    // Store the payload type for the outbound request. Needed for deserialization.
    private final Class<OUTBOUND> outboundPayloadType;

    // The underlying dispatcher. In this case io.nats.client.Dispatcher.
    private Dispatcher dispatcher;

    /**
     * Instantiate a new OCPPRequestHandler with the INBOUND and OUTBOUND payload types.
     * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
     * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
     */
    public OCPPRequestHandler(Class<INBOUND> inPayloadType, Class<OUTBOUND> outPayloadType) {
        this.inboundPayloadType = inPayloadType;
        this.outboundPayloadType = outPayloadType;
    }

    /**
     * Registers this request handler on the provided Connection.
     * The handler will start serving requests on the provided connection.
     * @param natsConnection
     * @see io.nats.client.Connection
     */
    public void register(Connection natsConnection) {
        if (dispatcher != null) return;

        this.dispatcher = natsConnection.createDispatcher((natsMsg) -> {
            String jsonPayload = new String(natsMsg.getData(), StandardCharsets.UTF_8);
            logger.info(String.format("Received request payload='%s' on subject '%s'",
                    jsonPayload,
                    natsMsg.getSubject()));

            ICallMessage<INBOUND> callMessage = deserialize(jsonPayload);

            // Handle the message internally, e.g. update state, store in db, etc.
            ICallResultMessage<OUTBOUND> callResult = handle(callMessage, natsMsg.getSubject());

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
    public void unregister() {
        dispatcher.unsubscribe(getRequestSubject());
    }

    /**
     * Serializes the {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage} to JSON.
     * @param message The {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage} to serialize.
     * @return The serialized message in JSON.
     */
    protected String serialize(ICallResultMessage<OUTBOUND> message) {
        try {
            return CallResultMessageSerializer.serialize(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Deserializes the raw payload in Json.
     * @param rawJsonPayload
     * @return
     */
    protected ICallMessage<INBOUND> deserialize(String rawJsonPayload) {
        ICallMessage<INBOUND> deserialized;

        try {
            deserialized = CallMessageDeserializer.deserialize(rawJsonPayload, inboundPayloadType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return deserialized;
    }

    /**
     * The method that contains the logic for how to handle the
     * {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage} payload.
     * The method must handle the CALL and provide a CALLRESULT
     * {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage}
     * @param message {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage}
     * @param subject The subject that the message was received on.
     * @return {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage}
     */
    public abstract ICallResultMessage<OUTBOUND> handle(ICallMessage<INBOUND> message, String subject);


    /**
     * The subject on which this request handler listen for incoming requests.
     * Must be overridden in subclasses.
     * E.g. operators.<opId>.csms.<csmsId>.cs.<csId>.requests.bootnotification
     *
     * Use the helper class {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver}.
     * @return
     */
    public abstract String getRequestSubject();
}
