package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.clients.exceptions.OCPPRequestException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallResultMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallResultMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.Headers;
import io.nats.client.impl.NatsMessage;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class OCPPRequester<OUTBOUND, INBOUND> {

    private final Logger logger = Logger.getLogger(OCPPRequester.class.getName());
    private final Class<OUTBOUND> outboundPayloadType;
    private final Class<INBOUND> inboundPayloadType;
    private Duration timeout = Duration.ofSeconds(30); // Default timeout

    /**
     * Instantiate a new OCPPRequester with the OUTBOUND and INBOUND payload types.
     *
     * @param inPayloadType  E.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest etc.
     * @param outPayloadType E.g. BootNotificationResponse, StatusNotificationResponse, SetChargingProfileResponse etc.
     */
    public OCPPRequester(Class<OUTBOUND> outPayloadType, Class<INBOUND> inPayloadType) {
        this.outboundPayloadType = outPayloadType;
        this.inboundPayloadType = inPayloadType;
    }

    /**
     * Sets the timeout for the request.
     * @param timeout
     */
    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * Serializes the {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage} to JSON.
     *
     * @param message The {@link dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage} to serialize.
     * @return The serialized message in JSON.
     */
    protected String serialize(ICallMessage<OUTBOUND> message) throws JsonProcessingException {
        return CallMessageSerializer.serialize(message);
    }

    /**
     * Deserializes the raw payload in Json.
     *
     * @param rawJsonPayload
     * @return
     */
    protected ICallResultMessage<INBOUND> deserialize(String rawJsonPayload) throws JsonProcessingException {
        return CallResultMessageDeserializer.deserialize(rawJsonPayload, inboundPayloadType);
    }


    /**
     * Sends the request to the subject on the provided connection.
     * This call is synchronous and waits for a response until the timeout is met.
     * @param request The request to send
     * @param requestSubject The subject the request is sent to
     * @param replyToSubject The subject that the receiver is expected to reply to.
     * @param natsConnection The NATS.io connection that is used to send the request and receive the response.
     * @return
     */
    public ICallResultMessage<INBOUND> request(ICallMessage<OUTBOUND> request, String requestSubject,
                                               String replyToSubject, Connection natsConnection) {
        try {
            String jsonRequestPayload = serialize(request);
            Message natsMessage = NatsMessage.builder()
                    .subject(requestSubject)
                    .replyTo(replyToSubject) // This is overwritten to a unique UUID by NATS.io when using their // request API.
                    .headers(new Headers()
                            .add("replyTo", replyToSubject) // Put the response subject into the header for traceability purposes.
                    )
                    .data(jsonRequestPayload, StandardCharsets.UTF_8)
                    .build();

            logger.info(String.format("Sending request '%s' with payload %s on subject %s",
                    outboundPayloadType.getName(), jsonRequestPayload, requestSubject));
            CompletableFuture<Message> response = natsConnection.requestWithTimeout(natsMessage, timeout);

            // Blocks until message is received.
            Message message = response.get();
            String jsonResponsePayload = new String(message.getData(), StandardCharsets.UTF_8);
            ICallResultMessage<INBOUND> callResult = deserialize(jsonResponsePayload);

            logger.info(String.format("Received response '%s' with payload %s on subject %s",
                    inboundPayloadType.getName(), jsonResponsePayload,
                    message.getSubject()));

            return callResult;
        } catch (InterruptedException | ExecutionException e) {
            // TODO: Handle if the CompletableFuture is interrupted or fails.
            logger.severe(e.getMessage());
            throw new OCPPRequestException(e.getMessage(), e.getCause());
        } catch (JsonProcessingException e) {
            // TODO: Handle if the deserialization fails for CallResult, try and deserialize to CallError
            logger.severe(e.getMessage());
            throw new OCPPRequestException(e.getMessage(), e.getCause());
        } catch (CancellationException e) {
            // TODO: Handle if the CompletableFuture got cancelled. This can happen if there are no subscribers to
            //  handle the request.
            logger.severe("The request got cancelled. This exception may happen if there are no subscribers to handle" +
                    " the request.");
            throw new OCPPRequestException(e.getMessage(), e.getCause());
        }
    }
}
