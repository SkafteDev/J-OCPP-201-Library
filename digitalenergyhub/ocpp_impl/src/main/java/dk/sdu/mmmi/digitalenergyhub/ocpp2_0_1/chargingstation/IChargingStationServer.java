package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.interfaces.IMessageHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes.OCPPMessageToSubjectMapping.OCPPMessageType;

/**
 * This interface represents a Charging Station (CS) that
 * can exchange messages with a Charging Station Management System (CSMS).
 *
 * @param <T> The general type of the messages that are exchanged.
 */
public interface IChargingStationServer<T> {

    /**
     * Returns the Operator ID that owns this Charging Station.
     *
     * @return
     */
    String getOperatorId();

    /**
     * Returns the Charging Station Management System (CSMS) ID that owns this Charging Station.
     *
     * @return
     */
    String getCsmsId();

    /**
     * Returns the Charging Station (CS) ID of this charging Station.
     *
     * @return
     */
    String getCsId();

    void addRequestHandler(OCPPMessageType ocppMessageType, IMessageHandler<T> handler);

    void emitMessage(OCPPMessageType ocppMessageType, T message);

    default String getResponsesChannel() {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("responses");

        return sb.toString();
    }

    default String getResponseChannel(OCPPMessageType msgType) {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("responses")
                .append(".")
                .append(msgType.toString().toLowerCase());

        return sb.toString();
    }

    default String getRequestsChannel() {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("requests");

        return sb.toString();
    }

    default String getRequestChannel(OCPPMessageType msgType) {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("requests")
                .append(".")
                .append(msgType.toString().toLowerCase());

        return sb.toString();
    }

    default String getEventsChannel() {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("events");

        return sb.toString();
    }

    default String getEventsChannel(OCPPMessageType msgType) {
        StringBuilder sb = new StringBuilder();
        sb.append("operators").append(".")
                .append(getOperatorId()).append(".")
                .append("csms").append(".")
                .append(getCsmsId()).append(".")
                .append("cs").append(".")
                .append(getCsId()).append(".")
                .append("events")
                .append(".")
                .append(msgType.toString().toLowerCase());

        return sb.toString();
    }
}
