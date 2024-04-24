package dk.sdu.mmmi.anylogic.messaging;

import java.util.HashMap;
import java.util.Map;

public class MessageRouteResolverImpl implements IMessageRouteResolver {

    private static final String SERVER_INSTANCE_ID_TOKEN = "{serverInstanceId}";
    private static final String REQUEST_TYPE_TOKEN = "{requesttype}";
    private static final String RESPONSE_TYPE_TOKEN = "{responsetype}";

    private final String serverInstanceId;
    private String requestTemplate;
    private String responseTemplate;

    private final Map<AnyLogicMessageType, String> routes;


    /**
     * Instantiates a new MessageRoutingMap to build request/response + event subject routes.
     */
    public MessageRouteResolverImpl(String serverInstanceId) {
        this.serverInstanceId = serverInstanceId;
        this.routes = new HashMap<>();

        initializeRouteTemplates();

        setupRoutingMap();
    }

    private void setupRoutingMap() {
        routes.put(AnyLogicMessageType.StartAnyLogicProcessRequest, createRoute(AnyLogicMessageType.StartAnyLogicProcessRequest));
        routes.put(AnyLogicMessageType.RunExperimentRequest, createRoute(AnyLogicMessageType.RunExperimentRequest));
    }

    private void initializeRouteTemplates() {
        this.requestTemplate = "anylogic." +
                SERVER_INSTANCE_ID_TOKEN + "." +
                "requests." +
                REQUEST_TYPE_TOKEN;

        this.responseTemplate = "anylogic." +
                SERVER_INSTANCE_ID_TOKEN + "." +
                "responses." +
                RESPONSE_TYPE_TOKEN;
    }

    private String createRoute(AnyLogicMessageType anyLogicMessageType) {
        String route;

        if (anyLogicMessageType.name().toLowerCase().contains("request")) {
            route = requestTemplate.replace(REQUEST_TYPE_TOKEN, anyLogicMessageType.getValue());

        } else if (anyLogicMessageType.name().toLowerCase().contains("response")) {
            route = responseTemplate.replace(RESPONSE_TYPE_TOKEN, anyLogicMessageType.getValue());
        } else {
            throw new RuntimeException("Could not create route for the given message type.");
        }

        route = route.replace(SERVER_INSTANCE_ID_TOKEN, serverInstanceId);

        return route.toLowerCase().replace(" ", "");
    }

    public String getRoute(AnyLogicMessageType msgType) {
        return routes.get(msgType);
    }

}
