package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.routes;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;

import java.util.HashMap;
import java.util.Map;

public class MessageRouteResolverImpl implements IMessageRouteResolver {

    private static final String OPERATOR_ID_TOKEN = "{operatorid}";
    private static final String CSMS_ID_TOKEN = "{csmsid}";
    private static final String CS_ID_TOKEN = "{csid}";
    private static final String REQUEST_TYPE_TOKEN = "{requesttype}";
    private static final String RESPONSE_TYPE_TOKEN = "{responsetype}";

    private final String operatorId;
    private final String csmsId;
    private final String csId;
    private String requestTemplate;
    private String responseTemplate;

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
    public MessageRouteResolverImpl(String operatorId, String csmsId, String csId) {
        this.operatorId = operatorId;
        this.csmsId = csmsId;
        this.csId = csId;
        this.routes = new HashMap<>();

        initializeRouteTemplates();

        setupRoutingMap();
    }

    private void setupRoutingMap() {
        routes.put(OCPPMessageType.AuthorizeRequest, createRoute(OCPPMessageType.AuthorizeRequest));
        routes.put(OCPPMessageType.AuthorizeResponse, createRoute(OCPPMessageType.AuthorizeResponse));
        routes.put(OCPPMessageType.BootNotificationRequest, createRoute(OCPPMessageType.BootNotificationRequest));
        routes.put(OCPPMessageType.BootNotificationResponse, createRoute(OCPPMessageType.BootNotificationResponse));
        routes.put(OCPPMessageType.CancelReservationRequest, createRoute(OCPPMessageType.CancelReservationRequest));
        routes.put(OCPPMessageType.CancelReservationResponse, createRoute(OCPPMessageType.CancelReservationResponse));
        routes.put(OCPPMessageType.CertificateSignedRequest, createRoute(OCPPMessageType.CertificateSignedRequest));
        routes.put(OCPPMessageType.CertificateSignedResponse, createRoute(OCPPMessageType.CertificateSignedResponse));
        routes.put(OCPPMessageType.ChangeAvailabilityRequest, createRoute(OCPPMessageType.ChangeAvailabilityRequest));
        routes.put(OCPPMessageType.ChangeAvailabilityResponse, createRoute(OCPPMessageType.ChangeAvailabilityResponse));
        routes.put(OCPPMessageType.ClearCacheRequest, createRoute(OCPPMessageType.ClearCacheRequest));
        routes.put(OCPPMessageType.ClearCacheResponse, createRoute(OCPPMessageType.ClearCacheResponse));
        routes.put(OCPPMessageType.ClearDisplayMessageRequest, createRoute(OCPPMessageType.ClearDisplayMessageRequest));
        routes.put(OCPPMessageType.ClearDisplayMessageResponse, createRoute(OCPPMessageType.ClearDisplayMessageResponse));
        routes.put(OCPPMessageType.ClearedChargingLimitRequest, createRoute(OCPPMessageType.ClearedChargingLimitRequest));
        routes.put(OCPPMessageType.ClearedChargingLimitResponse, createRoute(OCPPMessageType.ClearedChargingLimitResponse));
        routes.put(OCPPMessageType.ClearChargingProfileRequest, createRoute(OCPPMessageType.ClearChargingProfileRequest));
        routes.put(OCPPMessageType.ClearChargingProfileResponse, createRoute(OCPPMessageType.ClearChargingProfileResponse));
        routes.put(OCPPMessageType.ClearVariableMonitoringRequest, createRoute(OCPPMessageType.ClearVariableMonitoringRequest));
        routes.put(OCPPMessageType.ClearVariableMonitoringResponse, createRoute(OCPPMessageType.ClearVariableMonitoringResponse));
        routes.put(OCPPMessageType.CostUpdatedRequest, createRoute(OCPPMessageType.CostUpdatedRequest));
        routes.put(OCPPMessageType.CostUpdatedResponse, createRoute(OCPPMessageType.CostUpdatedResponse));
        routes.put(OCPPMessageType.CustomerInformationRequest, createRoute(OCPPMessageType.CustomerInformationRequest));
        routes.put(OCPPMessageType.CustomerInformationResponse, createRoute(OCPPMessageType.CustomerInformationResponse));
        routes.put(OCPPMessageType.DataTransferRequest, createRoute(OCPPMessageType.DataTransferRequest));
        routes.put(OCPPMessageType.DataTransferResponse, createRoute(OCPPMessageType.DataTransferResponse));
        routes.put(OCPPMessageType.DeleteCertificateRequest, createRoute(OCPPMessageType.DeleteCertificateRequest));
        routes.put(OCPPMessageType.DeleteCertificateResponse, createRoute(OCPPMessageType.DeleteCertificateResponse));
        routes.put(OCPPMessageType.FirmwareStatusNotificationRequest, createRoute(OCPPMessageType.FirmwareStatusNotificationRequest));
        routes.put(OCPPMessageType.FirmwareStatusNotificationResponse, createRoute(OCPPMessageType.FirmwareStatusNotificationResponse));
        routes.put(OCPPMessageType.Get15118EVCertificateRequest, createRoute(OCPPMessageType.Get15118EVCertificateRequest));
        routes.put(OCPPMessageType.Get15118EVCertificateResponse, createRoute(OCPPMessageType.Get15118EVCertificateResponse));
        routes.put(OCPPMessageType.GetBaseReportRequest, createRoute(OCPPMessageType.GetBaseReportRequest));
        routes.put(OCPPMessageType.GetBaseReportResponse, createRoute(OCPPMessageType.GetBaseReportResponse));
        routes.put(OCPPMessageType.GetCertificateStatusRequest, createRoute(OCPPMessageType.GetCertificateStatusRequest));
        routes.put(OCPPMessageType.GetCertificateStatusResponse, createRoute(OCPPMessageType.GetCertificateStatusResponse));
        routes.put(OCPPMessageType.GetChargingProfilesRequest, createRoute(OCPPMessageType.GetChargingProfilesRequest));
        routes.put(OCPPMessageType.GetChargingProfilesResponse, createRoute(OCPPMessageType.GetChargingProfilesResponse));
        routes.put(OCPPMessageType.GetCompositeScheduleRequest, createRoute(OCPPMessageType.GetCompositeScheduleRequest));
        routes.put(OCPPMessageType.GetCompositeScheduleResponse, createRoute(OCPPMessageType.GetCompositeScheduleResponse));
        routes.put(OCPPMessageType.GetDisplayMessagesRequest, createRoute(OCPPMessageType.GetDisplayMessagesRequest));
        routes.put(OCPPMessageType.GetDisplayMessagesResponse, createRoute(OCPPMessageType.GetDisplayMessagesResponse));
        routes.put(OCPPMessageType.GetInstalledCertificateIdsRequest, createRoute(OCPPMessageType.GetInstalledCertificateIdsRequest));
        routes.put(OCPPMessageType.GetInstalledCertificateIdsResponse, createRoute(OCPPMessageType.GetInstalledCertificateIdsResponse));
        routes.put(OCPPMessageType.GetLocalListVersionRequest, createRoute(OCPPMessageType.GetLocalListVersionRequest));
        routes.put(OCPPMessageType.GetLocalListVersionResponse, createRoute(OCPPMessageType.GetLocalListVersionResponse));
        routes.put(OCPPMessageType.GetLogRequest, createRoute(OCPPMessageType.GetLogRequest));
        routes.put(OCPPMessageType.GetLogResponse, createRoute(OCPPMessageType.GetLogResponse));
        routes.put(OCPPMessageType.GetMonitoringReportRequest, createRoute(OCPPMessageType.GetMonitoringReportRequest));
        routes.put(OCPPMessageType.GetMonitoringReportResponse, createRoute(OCPPMessageType.GetMonitoringReportResponse));
        routes.put(OCPPMessageType.GetReportRequest, createRoute(OCPPMessageType.GetReportRequest));
        routes.put(OCPPMessageType.GetReportResponse, createRoute(OCPPMessageType.GetReportResponse));
        routes.put(OCPPMessageType.GetTransactionStatusRequest, createRoute(OCPPMessageType.GetTransactionStatusRequest));
        routes.put(OCPPMessageType.GetTransactionStatusResponse, createRoute(OCPPMessageType.GetTransactionStatusResponse));
        routes.put(OCPPMessageType.GetVariablesRequest, createRoute(OCPPMessageType.GetVariablesRequest));
        routes.put(OCPPMessageType.GetVariablesResponse, createRoute(OCPPMessageType.GetVariablesResponse));
        routes.put(OCPPMessageType.HeartbeatRequest, createRoute(OCPPMessageType.HeartbeatRequest));
        routes.put(OCPPMessageType.HeartbeatResponse, createRoute(OCPPMessageType.HeartbeatResponse));
        routes.put(OCPPMessageType.InstallCertificateRequest, createRoute(OCPPMessageType.InstallCertificateRequest));
        routes.put(OCPPMessageType.InstallCertificateResponse, createRoute(OCPPMessageType.InstallCertificateResponse));
        routes.put(OCPPMessageType.LogStatusNotificationRequest, createRoute(OCPPMessageType.LogStatusNotificationRequest));
        routes.put(OCPPMessageType.LogStatusNotificationResponse, createRoute(OCPPMessageType.LogStatusNotificationResponse));
        routes.put(OCPPMessageType.MeterValuesRequest, createRoute(OCPPMessageType.MeterValuesRequest));
        routes.put(OCPPMessageType.MeterValuesResponse, createRoute(OCPPMessageType.MeterValuesResponse));
        routes.put(OCPPMessageType.NotifyChargingLimitRequest, createRoute(OCPPMessageType.NotifyChargingLimitRequest));
        routes.put(OCPPMessageType.NotifyChargingLimitResponse, createRoute(OCPPMessageType.NotifyChargingLimitResponse));
        routes.put(OCPPMessageType.NotifyCustomerInformationRequest, createRoute(OCPPMessageType.NotifyCustomerInformationRequest));
        routes.put(OCPPMessageType.NotifyCustomerInformationResponse, createRoute(OCPPMessageType.NotifyCustomerInformationResponse));
        routes.put(OCPPMessageType.NotifyDisplayMessagesRequest, createRoute(OCPPMessageType.NotifyDisplayMessagesRequest));
        routes.put(OCPPMessageType.NotifyDisplayMessagesResponse, createRoute(OCPPMessageType.NotifyDisplayMessagesResponse));
        routes.put(OCPPMessageType.NotifyEVChargingNeedsRequest, createRoute(OCPPMessageType.NotifyEVChargingNeedsRequest));
        routes.put(OCPPMessageType.NotifyEVChargingNeedsResponse, createRoute(OCPPMessageType.NotifyEVChargingNeedsResponse));
        routes.put(OCPPMessageType.NotifyEVChargingScheduleRequest, createRoute(OCPPMessageType.NotifyEVChargingScheduleRequest));
        routes.put(OCPPMessageType.NotifyEVChargingScheduleResponse, createRoute(OCPPMessageType.NotifyEVChargingScheduleResponse));
        routes.put(OCPPMessageType.NotifyEventRequest, createRoute(OCPPMessageType.NotifyEventRequest));
        routes.put(OCPPMessageType.NotifyEventResponse, createRoute(OCPPMessageType.NotifyEventResponse));
        routes.put(OCPPMessageType.NotifyMonitoringReportRequest, createRoute(OCPPMessageType.NotifyMonitoringReportRequest));
        routes.put(OCPPMessageType.NotifyMonitoringReportResponse, createRoute(OCPPMessageType.NotifyMonitoringReportResponse));
        routes.put(OCPPMessageType.NotifyReportRequest, createRoute(OCPPMessageType.NotifyReportRequest));
        routes.put(OCPPMessageType.NotifyReportResponse, createRoute(OCPPMessageType.NotifyReportResponse));
        routes.put(OCPPMessageType.PublishFirmwareRequest, createRoute(OCPPMessageType.PublishFirmwareRequest));
        routes.put(OCPPMessageType.PublishFirmwareResponse, createRoute(OCPPMessageType.PublishFirmwareResponse));
        routes.put(OCPPMessageType.PublishFirmwareStatusNotificationRequest, createRoute(OCPPMessageType.PublishFirmwareStatusNotificationRequest));
        routes.put(OCPPMessageType.PublishFirmwareStatusNotificationResponse, createRoute(OCPPMessageType.PublishFirmwareStatusNotificationResponse));
        routes.put(OCPPMessageType.ReportChargingProfilesRequest, createRoute(OCPPMessageType.ReportChargingProfilesRequest));
        routes.put(OCPPMessageType.ReportChargingProfilesResponse, createRoute(OCPPMessageType.ReportChargingProfilesResponse));
        routes.put(OCPPMessageType.RequestStartTransactionRequest, createRoute(OCPPMessageType.RequestStartTransactionRequest));
        routes.put(OCPPMessageType.RequestStartTransactionResponse, createRoute(OCPPMessageType.RequestStartTransactionResponse));
        routes.put(OCPPMessageType.RequestStopTransactionRequest, createRoute(OCPPMessageType.RequestStopTransactionRequest));
        routes.put(OCPPMessageType.RequestStopTransactionResponse, createRoute(OCPPMessageType.RequestStopTransactionResponse));
        routes.put(OCPPMessageType.ReservationStatusUpdateRequest, createRoute(OCPPMessageType.ReservationStatusUpdateRequest));
        routes.put(OCPPMessageType.ReservationStatusUpdateResponse, createRoute(OCPPMessageType.ReservationStatusUpdateResponse));
        routes.put(OCPPMessageType.ReserveNowRequest, createRoute(OCPPMessageType.ReserveNowRequest));
        routes.put(OCPPMessageType.ReserveNowResponse, createRoute(OCPPMessageType.ReserveNowResponse));
        routes.put(OCPPMessageType.ResetRequest, createRoute(OCPPMessageType.ResetRequest));
        routes.put(OCPPMessageType.ResetResponse, createRoute(OCPPMessageType.ResetResponse));
        routes.put(OCPPMessageType.SecurityEventNotificationRequest, createRoute(OCPPMessageType.SecurityEventNotificationRequest));
        routes.put(OCPPMessageType.SecurityEventNotificationResponse, createRoute(OCPPMessageType.SecurityEventNotificationResponse));
        routes.put(OCPPMessageType.SendLocalListRequest, createRoute(OCPPMessageType.SendLocalListRequest));
        routes.put(OCPPMessageType.SendLocalListResponse, createRoute(OCPPMessageType.SendLocalListResponse));
        routes.put(OCPPMessageType.SetChargingProfileRequest, createRoute(OCPPMessageType.SetChargingProfileRequest));
        routes.put(OCPPMessageType.SetChargingProfileResponse, createRoute(OCPPMessageType.SetChargingProfileResponse));
        routes.put(OCPPMessageType.SetDisplayMessageRequest, createRoute(OCPPMessageType.SetDisplayMessageRequest));
        routes.put(OCPPMessageType.SetDisplayMessageResponse, createRoute(OCPPMessageType.SetDisplayMessageResponse));
        routes.put(OCPPMessageType.SetMonitoringBaseRequest, createRoute(OCPPMessageType.SetMonitoringBaseRequest));
        routes.put(OCPPMessageType.SetMonitoringBaseResponse, createRoute(OCPPMessageType.SetMonitoringBaseResponse));
        routes.put(OCPPMessageType.SetMonitoringLevelRequest, createRoute(OCPPMessageType.SetMonitoringLevelRequest));
        routes.put(OCPPMessageType.SetMonitoringLevelResponse, createRoute(OCPPMessageType.SetMonitoringLevelResponse));
        routes.put(OCPPMessageType.SetNetworkProfileRequest, createRoute(OCPPMessageType.SetNetworkProfileRequest));
        routes.put(OCPPMessageType.SetNetworkProfileResponse, createRoute(OCPPMessageType.SetNetworkProfileResponse));
        routes.put(OCPPMessageType.SetVariableMonitoringRequest, createRoute(OCPPMessageType.SetVariableMonitoringRequest));
        routes.put(OCPPMessageType.SetVariableMonitoringResponse, createRoute(OCPPMessageType.SetVariableMonitoringResponse));
        routes.put(OCPPMessageType.SetVariablesRequest, createRoute(OCPPMessageType.SetVariablesRequest));
        routes.put(OCPPMessageType.SetVariablesResponse, createRoute(OCPPMessageType.SetVariablesResponse));
        routes.put(OCPPMessageType.SignCertificateRequest, createRoute(OCPPMessageType.SignCertificateRequest));
        routes.put(OCPPMessageType.SignCertificateResponse, createRoute(OCPPMessageType.SignCertificateResponse));
        routes.put(OCPPMessageType.StatusNotificationRequest, createRoute(OCPPMessageType.StatusNotificationRequest));
        routes.put(OCPPMessageType.StatusNotificationResponse, createRoute(OCPPMessageType.StatusNotificationResponse));
        routes.put(OCPPMessageType.TransactionEventRequest, createRoute(OCPPMessageType.TransactionEventRequest));
        routes.put(OCPPMessageType.TransactionEventResponse, createRoute(OCPPMessageType.TransactionEventResponse));
        routes.put(OCPPMessageType.TriggerMessageRequest, createRoute(OCPPMessageType.TriggerMessageRequest));
        routes.put(OCPPMessageType.TriggerMessageResponse, createRoute(OCPPMessageType.TriggerMessageResponse));
        routes.put(OCPPMessageType.UnlockConnectorRequest, createRoute(OCPPMessageType.UnlockConnectorRequest));
        routes.put(OCPPMessageType.UnlockConnectorResponse, createRoute(OCPPMessageType.UnlockConnectorResponse));
        routes.put(OCPPMessageType.UnpublishFirmwareRequest, createRoute(OCPPMessageType.UnpublishFirmwareRequest));
        routes.put(OCPPMessageType.UnpublishFirmwareResponse, createRoute(OCPPMessageType.UnpublishFirmwareResponse));
        routes.put(OCPPMessageType.UpdateFirmwareRequest, createRoute(OCPPMessageType.UpdateFirmwareRequest));
        routes.put(OCPPMessageType.UpdateFirmwareResponse, createRoute(OCPPMessageType.UpdateFirmwareResponse));
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

        return route.toLowerCase().replace(" ", "");
    }

    public String getRoute(OCPPMessageType msgType) {
        return routes.get(msgType);
    }

    public String getRequestRoute() {
        String route = requestTemplate.replace(OPERATOR_ID_TOKEN, operatorId)
                .replace(CSMS_ID_TOKEN, csmsId)
                .replace(CS_ID_TOKEN, csId)
                .replace(REQUEST_TYPE_TOKEN, "*");


        return route.toLowerCase().replace(" ", "");
    }

    public String getResponseRoute() {
        String route = responseTemplate.replace(OPERATOR_ID_TOKEN, operatorId)
                .replace(CSMS_ID_TOKEN, csmsId)
                .replace(CS_ID_TOKEN, csId)
                .replace(RESPONSE_TYPE_TOKEN, "*");


        return route.toLowerCase().replace(" ", "");
    }

}
