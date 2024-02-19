package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes;

import java.util.HashMap;
import java.util.Map;

public class MessageRoutingMap {

    private static final String OPERATOR_ID_TOKEN = "{operatorid}";
    private static final String CSMS_ID_TOKEN = "{csmsid}";
    private static final String CS_ID_TOKEN = "{csid}";
    private static final String REQUEST_TYPE_TOKEN = "{requesttype}";
    private static final String RESPONSE_TYPE_TOKEN = "{responsetype}";
    private static final String EVENT_TYPE_TOKEN = "{eventtype}";

    private final String operatorId;
    private final String csmsId;
    private final String csId;
    private String requestTemplate;
    private String responseTemplate;
    private String eventTemplate;

    private final Map<OCPPMessageType, String> routes;


    /**
     * Instantiates a new MessageRoutingMap to build request/response + event subject routes.
     *
     * @param operatorId The operator ID. For instance, Clever, EWII, OK, etc.
     * @param csmsId     The Charging Station Management System ID. For example, a UUID or another identifier that can be
     *                   used to identify a Charging Station Management System.
     * @param csId       The Charging Station ID. For example, a UUID or another identifier that can be used to identify a
     *                   Charging Station.
     */
    public MessageRoutingMap(String operatorId, String csmsId, String csId) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.csId = csId;
        this.routes = new HashMap<>();

        initializeRouteTemplates();

        setupRoutingMap();
    }

    private void setupRoutingMap() {
        // TODO: Add routes for all types of OCPP 2.0.1 messages.
        routes.put(OCPPMessageType.AuthorizeRequest, createRoute(OCPPMessageType.AuthorizeRequest));
        routes.put(OCPPMessageType.AuthorizeResponse, createRoute(OCPPMessageType.AuthorizeResponse));
        routes.put(OCPPMessageType.BootNotificationRequest, createRoute(OCPPMessageType.BootNotificationRequest));
        routes.put(OCPPMessageType.BootNotificationResponse, createRoute(OCPPMessageType.BootNotificationResponse));
        routes.put(OCPPMessageType.ClearChargingProfileRequest, createRoute(OCPPMessageType.ClearChargingProfileRequest));
        routes.put(OCPPMessageType.ClearChargingProfileResponse, createRoute(OCPPMessageType.ClearChargingProfileResponse));
        routes.put(OCPPMessageType.SetChargingProfileRequest, createRoute(OCPPMessageType.SetChargingProfileRequest));
        routes.put(OCPPMessageType.SetChargingProfileResponse, createRoute(OCPPMessageType.SetChargingProfileResponse));
    }

    private void initializeRouteTemplates() {
        this.requestTemplate = "operators." +
                OPERATOR_ID_TOKEN + "." +
                "csms." +
                CSMS_ID_TOKEN + "." +
                "cs." +
                CS_ID_TOKEN + "." +
                "requests." +
                REQUEST_TYPE_TOKEN;

        this.responseTemplate = "operators." +
                OPERATOR_ID_TOKEN + "." +
                "csms." +
                CSMS_ID_TOKEN + "." +
                "cs." +
                CS_ID_TOKEN + "." +
                "responses." +
                RESPONSE_TYPE_TOKEN;

        this.eventTemplate = "operators." +
                OPERATOR_ID_TOKEN + "." +
                "csms." +
                CSMS_ID_TOKEN + "." +
                "cs." +
                CS_ID_TOKEN + "." +
                "events." +
                EVENT_TYPE_TOKEN;
    }

    private String createRoute(OCPPMessageType ocppMessageType) {
        String route;

        if (ocppMessageType.name().toLowerCase().contains("request")) {
            route = requestTemplate.replace(REQUEST_TYPE_TOKEN, ocppMessageType.getValue());

        } else if (ocppMessageType.name().toLowerCase().contains("response")) {
            route = responseTemplate.replace(RESPONSE_TYPE_TOKEN, ocppMessageType.getValue());
        } else {
            throw new RuntimeException("Could not create route for the given message type.");
        }

        route = route.replace(OPERATOR_ID_TOKEN, operatorId)
                .replace(CSMS_ID_TOKEN, csmsId)
                .replace(CS_ID_TOKEN, csId);

        return route.toLowerCase();
    }

    public String getRoute(OCPPMessageType msgType) {
        return routes.get(msgType);
    }

    public enum OCPPMessageType {
        // TODO: Add all types of OCPP 2.0.1 messages.
        AuthorizeRequest("Authorize"),
        AuthorizeResponse("Authorize"),
        BootNotificationRequest("BootNotification"),
        BootNotificationResponse("BootNotification"),
        ClearChargingProfileRequest("ClearChargingProfile"),
        ClearChargingProfileResponse("ClearChargingProfile"),
        SetChargingProfileRequest("SetChargingProfile"),
        SetChargingProfileResponse("SetChargingProfile");

        private final String value;

        OCPPMessageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
