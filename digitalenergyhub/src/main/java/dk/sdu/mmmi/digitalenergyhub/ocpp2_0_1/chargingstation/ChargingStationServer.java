package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.nats.NatsClient;
import io.nats.client.Message;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes.OCPPMessageToSubjectMapping.OCPPMessageType;

import java.util.logging.Logger;

public class ChargingStationServer implements IChargingStationServer<Message> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final String operatorId;
    private final String csmsId;
    private final String csId;
    private final String natsConnectionString;
    private final NatsClient natsClient;
    private final Thread natsClientThread;

    public ChargingStationServer(String operatorId, String csmsId, String csId, String natsConnectionString) {
        this.operatorId = sanitise(operatorId);
        this.csmsId = sanitise(csmsId);
        this.csId = sanitise(csId);
        this.natsConnectionString = natsConnectionString;
        this.natsClient = new NatsClient(natsConnectionString);

        // Start the NATS.io client on a new thread and start listening for incoming messages.
        this.natsClientThread = new Thread(natsClient::startConsume);
        this.natsClientThread.start();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getCsmsId() {
        return csmsId;
    }

    public String getCsId() {
        return csId;
    }

    private String sanitise(String in) {
        return in.replace(" ", "").toLowerCase();
    }

    @Override
    public void addRequestHandler(OCPPMessageType ocppMessageType, IMessageHandler<Message> handler) {
        String channelName = this.getRequestChannel(ocppMessageType);
        natsClient.addSubscriber(channelName, handler);
    }

    @Override
    public void emitResponse(OCPPMessageType ocppMessageType, Message response) {
        String channelName = this.getResponseChannel(ocppMessageType);
        natsClient.publish(channelName, response);
    }
}
