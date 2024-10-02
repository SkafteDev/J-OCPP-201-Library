/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.ocpp2_0_1.api;

public enum OCPPMessageType {
    // OCPP 2.0.1 message types
    AuthorizeRequest("AuthorizeRequest"),
    AuthorizeResponse("AuthorizeResponse"),
    BootNotificationRequest("BootNotificationRequest"),
    BootNotificationResponse("BootNotificationResponse"),
    CancelReservationRequest("CancelReservationRequest"),
    CancelReservationResponse("CancelReservationResponse"),
    CertificateSignedRequest("CertificateSignedRequest"),
    CertificateSignedResponse("CertificateSignedResponse"),
    ChangeAvailabilityRequest("ChangeAvailabilityRequest"),
    ChangeAvailabilityResponse("ChangeAvailabilityResponse"),
    ClearCacheRequest("ClearCacheRequest"),
    ClearCacheResponse("ClearCacheResponse"),
    ClearChargingProfileRequest("ClearChargingProfileRequest"),
    ClearChargingProfileResponse("ClearChargingProfileResponse"),
    ClearDisplayMessageRequest("ClearDisplayMessageRequest"),
    ClearDisplayMessageResponse("ClearDisplayMessageResponse"),
    ClearedChargingLimitRequest("ClearedChargingLimitRequest"),
    ClearedChargingLimitResponse("ClearedChargingLimitResponse"),
    ClearVariableMonitoringRequest("ClearVariableMonitoringRequest"),
    ClearVariableMonitoringResponse("ClearVariableMonitoringResponse"),
    CostUpdatedRequest("CostUpdatedRequest"),
    CostUpdatedResponse("CostUpdatedResponse"),
    CustomerInformationRequest("CustomerInformationRequest"),
    CustomerInformationResponse("CustomerInformationResponse"),
    DataTransferRequest("DataTransferRequest"),
    DataTransferResponse("DataTransferResponse"),
    DeleteCertificateRequest("DeleteCertificateRequest"),
    DeleteCertificateResponse("DeleteCertificateResponse"),
    FirmwareStatusNotificationRequest("FirmwareStatusNotificationRequest"),
    FirmwareStatusNotificationResponse("FirmwareStatusNotificationResponse"),
    Get15118EVCertificateRequest("Get15118EVCertificateRequest"),
    Get15118EVCertificateResponse("Get15118EVCertificateResponse"),
    GetBaseReportRequest("GetBaseReportRequest"),
    GetBaseReportResponse("GetBaseReportResponse"),
    GetCertificateStatusRequest("GetCertificateStatusRequest"),
    GetCertificateStatusResponse("GetCertificateStatusResponse"),
    GetChargingProfilesRequest("GetChargingProfilesRequest"),
    GetChargingProfilesResponse("GetChargingProfilesResponse"),
    GetCompositeScheduleRequest("GetCompositeScheduleRequest"),
    GetCompositeScheduleResponse("GetCompositeScheduleResponse"),
    GetDisplayMessagesRequest("GetDisplayMessagesRequest"),
    GetDisplayMessagesResponse("GetDisplayMessagesResponse"),
    GetInstalledCertificateIdsRequest("GetInstalledCertificateIdsRequest"),
    GetInstalledCertificateIdsResponse("GetInstalledCertificateIdsResponse"),
    GetLocalListVersionRequest("GetLocalListVersionRequest"),
    GetLocalListVersionResponse("GetLocalListVersionResponse"),
    GetLogRequest("GetLogRequest"),
    GetLogResponse("GetLogResponse"),
    GetMonitoringReportRequest("GetMonitoringReportRequest"),
    GetMonitoringReportResponse("GetMonitoringReportResponse"),
    GetReportRequest("GetReportRequest"),
    GetReportResponse("GetReportResponse"),
    GetTransactionStatusRequest("GetTransactionStatusRequest"),
    GetTransactionStatusResponse("GetTransactionStatusResponse"),
    GetVariablesRequest("GetVariablesRequest"),
    GetVariablesResponse("GetVariablesResponse"),
    HeartbeatRequest("HeartbeatRequest"),
    HeartbeatResponse("HeartbeatResponse"),
    InstallCertificateRequest("InstallCertificateRequest"),
    InstallCertificateResponse("InstallCertificateResponse"),
    LogStatusNotificationRequest("LogStatusNotificationRequest"),
    LogStatusNotificationResponse("LogStatusNotificationResponse"),
    MeterValuesRequest("MeterValuesRequest"),
    MeterValuesResponse("MeterValuesResponse"),
    NotifyChargingLimitRequest("NotifyChargingLimitRequest"),
    NotifyChargingLimitResponse("NotifyChargingLimitResponse"),
    NotifyCustomerInformationRequest("NotifyCustomerInformationRequest"),
    NotifyCustomerInformationResponse("NotifyCustomerInformationResponse"),
    NotifyDisplayMessagesRequest("NotifyDisplayMessagesRequest"),
    NotifyDisplayMessagesResponse("NotifyDisplayMessagesResponse"),
    NotifyEVChargingNeedsRequest("NotifyEVChargingNeedsRequest"),
    NotifyEVChargingNeedsResponse("NotifyEVChargingNeedsResponse"),
    NotifyEVChargingScheduleRequest("NotifyEVChargingScheduleRequest"),
    NotifyEVChargingScheduleResponse("NotifyEVChargingScheduleResponse"),
    NotifyEventRequest("NotifyEventRequest"),
    NotifyEventResponse("NotifyEventResponse"),
    NotifyMonitoringReportRequest("NotifyMonitoringReportRequest"),
    NotifyMonitoringReportResponse("NotifyMonitoringReportResponse"),
    NotifyReportRequest("NotifyReportRequest"),
    NotifyReportResponse("NotifyReportResponse"),
    PublishFirmwareRequest("PublishFirmwareRequest"),
    PublishFirmwareResponse("PublishFirmwareResponse"),
    PublishFirmwareStatusNotificationRequest("PublishFirmwareStatusNotificationRequest"),
    PublishFirmwareStatusNotificationResponse("PublishFirmwareStatusNotificationResponse"),
    ReportChargingProfilesRequest("ReportChargingProfilesRequest"),
    ReportChargingProfilesResponse("ReportChargingProfilesResponse"),
    RequestStartTransactionRequest("RequestStartTransactionRequest"),
    RequestStartTransactionResponse("RequestStartTransactionResponse"),
    RequestStopTransactionRequest("RequestStopTransactionRequest"),
    RequestStopTransactionResponse("RequestStopTransactionResponse"),
    ReservationStatusUpdateRequest("ReservationStatusUpdateRequest"),
    ReservationStatusUpdateResponse("ReservationStatusUpdateResponse"),
    ReserveNowRequest("ReserveNowRequest"),
    ReserveNowResponse("ReserveNowResponse"),
    ResetRequest("ResetRequest"),
    ResetResponse("ResetResponse"),
    SecurityEventNotificationRequest("SecurityEventNotificationRequest"),
    SecurityEventNotificationResponse("SecurityEventNotificationResponse"),
    SendLocalListRequest("SendLocalListRequest"),
    SendLocalListResponse("SendLocalListResponse"),
    SetChargingProfileRequest("SetChargingProfileRequest"),
    SetChargingProfileResponse("SetChargingProfileResponse"),
    SetDisplayMessageRequest("SetDisplayMessageRequest"),
    SetDisplayMessageResponse("SetDisplayMessageResponse"),
    SetMonitoringBaseRequest("SetMonitoringBaseRequest"),
    SetMonitoringBaseResponse("SetMonitoringBaseResponse"),
    SetMonitoringLevelRequest("SetMonitoringLevelRequest"),
    SetMonitoringLevelResponse("SetMonitoringLevelResponse"),
    SetNetworkProfileRequest("SetNetworkProfileRequest"),
    SetNetworkProfileResponse("SetNetworkProfileResponse"),
    SetVariableMonitoringRequest("SetVariableMonitoringRequest"),
    SetVariableMonitoringResponse("SetVariableMonitoringResponse"),
    SetVariablesRequest("SetVariablesRequest"),
    SetVariablesResponse("SetVariablesResponse"),
    SignCertificateRequest("SignCertificateRequest"),
    SignCertificateResponse("SignCertificateResponse"),
    StatusNotificationRequest("StatusNotificationRequest"),
    StatusNotificationResponse("StatusNotificationResponse"),
    TransactionEventRequest("TransactionEventRequest"),
    TransactionEventResponse("TransactionEventResponse"),
    TriggerMessageRequest("TriggerMessageRequest"),
    TriggerMessageResponse("TriggerMessageResponse"),
    UnlockConnectorRequest("UnlockConnectorRequest"),
    UnlockConnectorResponse("UnlockConnectorResponse"),
    UnpublishFirmwareRequest("UnpublishFirmwareRequest"),
    UnpublishFirmwareResponse("UnpublishFirmwareResponse"),
    UpdateFirmwareRequest("UpdateFirmwareRequest"),
    UpdateFirmwareResponse("UpdateFirmwareResponse");

    private final String value;

    OCPPMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * Returns the action as defined in OCPP 2.0.1.
     * Examples:
     * Action of 'StatusNotificationRequest' is 'StatusNotification'
     * Action of 'StatusNotificationResponse' is also 'StatusNotification'
     * @return
     */
    public String getAction() {
        return this.value
                .replace("Request", "")
                .replace("Response", "");
    }
}