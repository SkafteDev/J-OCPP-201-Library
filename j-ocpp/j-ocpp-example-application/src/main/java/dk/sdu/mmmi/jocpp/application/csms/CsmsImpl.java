package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ICsms;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ErrorCode;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallError;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallErrorImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallResultImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Logger;

public class CsmsImpl implements ICsms {

    /**
     * TODO:Implement a statechart that tracks whether a given request can be serviced.
     * Only a single request can be serviced at any given moment.
     */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final ISessionManager sessionManager;

    public CsmsImpl(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ICallResult<AuthorizeResponse> sendAuthorizeRequest(Headers headers, ICall<AuthorizeRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.AuthorizeRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<BootNotificationResponse> sendBootNotificationRequest(Headers headers, ICall<BootNotificationRequest> req) {
        boolean isCsIdPresent = headers.contains(Headers.HeaderEnum.CS_ID.getValue());

        return acceptBootNotificationRequest(req.getMessageId());
    }

    private ICallResult<BootNotificationResponse> acceptBootNotificationRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.ACCEPTED)
                .withCurrentTime(now)
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResultMessage =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }

    private ICallResult<BootNotificationResponse> rejectBootNotificationRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
                .withStatus(RegistrationStatusEnum.REJECTED)
                .withCurrentTime(now)
                .withInterval((int) Duration.ofMinutes(2).toSeconds())
                .build();

        ICallResult<BootNotificationResponse> callResultMessage =
                CallResultImpl.<BootNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(bootNotificationResponse)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<ClearedChargingLimitResponse> sendClearedChargingLimitRequest(Headers headers, ICall<ClearedChargingLimitRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ClearedChargingLimitRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<FirmwareStatusNotificationResponse> sendFirmwareStatusNotificationRequest(Headers headers, ICall<FirmwareStatusNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.FirmwareStatusNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<Get15118EVCertificateResponse> sendGet15118EVCertificateRequest(Headers headers, ICall<Get15118EVCertificateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.Get15118EVCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<GetCertificateStatusResponse> sendGetCertificateStatusRequest(Headers headers, ICall<GetCertificateStatusRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.GetCertificateStatusRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<HeartbeatResponse> sendHeartbeatRequest(Headers headers, ICall<HeartbeatRequest> req) {
        return acceptHeartbeatRequest(req.getMessageId());
    }

    private ICallResult<HeartbeatResponse> acceptHeartbeatRequest(String responseMsgId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        HeartbeatResponse response = HeartbeatResponse.builder()
                .withCurrentTime(now)
                .build();

        ICallResult<HeartbeatResponse> callResultMessage =
                CallResultImpl.<HeartbeatResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(response)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<LogStatusNotificationResponse> sendLogStatusNotificationRequest(Headers headers, ICall<LogStatusNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.LogStatusNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyChargingLimitResponse> sendNotifyChargingLimitRequest(Headers headers, ICall<NotifyChargingLimitRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyChargingLimitRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyCustomerInformationResponse> sendNotifyCustomerInformationRequest(Headers headers, ICall<NotifyCustomerInformationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyCustomerInformationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyDisplayMessagesResponse> sendNotifyDisplayMessagesRequest(Headers headers, ICall<NotifyDisplayMessagesRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyDisplayMessagesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEVChargingNeedsResponse> sendNotifyEVChargingNeedsRequest(Headers headers, ICall<NotifyEVChargingNeedsRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEVChargingNeedsRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEVChargingScheduleResponse> sendNotifyEVChargingScheduleRequest(Headers headers, ICall<NotifyEVChargingScheduleRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEVChargingScheduleRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyEventResponse> sendNotifyEventRequest(Headers headers, ICall<NotifyEventRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyEventRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyMonitoringReportResponse> sendNotifyMonitoringReportRequest(Headers headers, ICall<NotifyMonitoringReportRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyMonitoringReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<NotifyReportResponse> sendNotifyReportRequest(Headers headers, ICall<NotifyReportRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.NotifyReportRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<PublishFirmwareResponse> sendPublishFirmwareRequest(Headers headers, ICall<PublishFirmwareRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.PublishFirmwareRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ReportChargingProfilesResponse> sendReportChargingProfilesRequest(Headers headers, ICall<ReportChargingProfilesRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ReportChargingProfilesRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<ReservationStatusUpdateResponse> sendReservationStatusUpdateRequest(Headers headers, ICall<ReservationStatusUpdateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.ReservationStatusUpdateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SecurityEventNotificationResponse> sendSecurityEventNotificationRequest(Headers headers, ICall<SecurityEventNotificationRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SecurityEventNotificationRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<SignCertificateResponse> sendSignCertificateRequest(Headers headers, ICall<SignCertificateRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.SignCertificateRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }

    @Override
    public ICallResult<StatusNotificationResponse> sendStatusNotificationRequest(Headers headers, ICall<StatusNotificationRequest> req) {
        return acceptStatusNotificationRequest(req.getMessageId());
    }

    private ICallResult<StatusNotificationResponse> acceptStatusNotificationRequest(String responseMsgId) {
        StatusNotificationResponse statusNotificationResponse = StatusNotificationResponse.builder()
                // No fields required as specified in OCPP 2.0.1.
                .build();

        ICallResult<StatusNotificationResponse> callResultMessage =
                CallResultImpl.<StatusNotificationResponse>newBuilder()
                        .withMessageId(responseMsgId) // NB! Important to reuse the message ID for traceability
                        .withPayLoad(statusNotificationResponse)
                        .build();

        return callResultMessage;
    }

    @Override
    public ICallResult<TransactionEventResponse> sendTransactionEventRequest(Headers headers, ICall<TransactionEventRequest> req) {
        ICallError notImplemented = CallErrorImpl.newCallErrorBuilder()
                .withMessageId(req.getMessageId())
                .withErrorCode(ErrorCode.NOT_IMPLEMENTED)
                .withErrorDescription(String.format("%s not implemented.", OCPPMessageType.TransactionEventRequest))
                .withErrorDetails("{}") // Must be of type JSON object.
                .build();

        return notImplemented;
    }


    /**
     * Returns a map of Charging Station IDs and for each, a generated Charging Profile.
     *
     * @return
     */
    private Map<String, ChargingProfile> calculateChargingProfiles() {
        Map<String, ChargingProfile> profileMap = new HashMap<>();

        try {
            // Simulate a long-running task (calculating charging profiles)
            double taskLength = Math.random() * 30d;
            Thread.sleep((int) taskLength);

            for (String csIdentifier : sessionManager.getSessionIds()) {
                ChargingProfile chargingProfile = getChargingProfile();
                profileMap.put(csIdentifier, chargingProfile);
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return profileMap;
    }

    private ChargingProfile getChargingProfile() {
        return ChargingProfile.builder()
                .withId(1)
                .withStackLevel(0)
                .withChargingProfilePurpose(ChargingProfilePurposeEnum.CHARGING_STATION_MAX_PROFILE)
                .withChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE)
                .withChargingSchedule(
                        List.of(
                                ChargingSchedule.builder()
                                        .withId(1)
                                        .withStartSchedule(ZonedDateTime.of(2024, 1, 20, 0, 0, 0, 0, ZoneId.systemDefault()))
                                        .withChargingRateUnit(ChargingRateUnitEnum.W)
                                        /*
                                         * From this point is the limit for each hour in the day
                                         */
                                        .withChargingSchedulePeriod(List.of(
                                                // From 00:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(0 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 01:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(1 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 02:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(2 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 03:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(3 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 04:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(4 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 05:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(5 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 06:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(6 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 07:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(7 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 08:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(8 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 09:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(9 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 10:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(10 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 11:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(11 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 12:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(12 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 13:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(13 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 14:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(14 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 15:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(15 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 16:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(16 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 17:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(17 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 18:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(18 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 19:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(19 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 20:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(20 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 21:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(21 * 60 * 60)
                                                        .withLimit(0d)
                                                        .build(),
                                                // From 22:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(22 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build(),
                                                // From 23:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(23 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .build()
                                        )).build()
                        ))
                .build();
    }

    public void startSmartChargingControlLoop(Duration interval) {
        // Control loop
        while (true) {
            long startTime = System.currentTimeMillis();
            logger.info(String.format("Running Smart Charging control loop with interval=%s seconds.",
                    interval.toSeconds()));

            // Regular control flow
            logger.info("Calculating ChargingProfiles.");
            Map<String, ChargingProfile> profileMap = calculateChargingProfiles();
            logger.info("Finished calculation of ChargingProfiles.");
            emitChargingProfiles(profileMap);

            // Calculate the time spent controlling.
            Duration elapsed = Duration.ofMillis(System.currentTimeMillis() - startTime);
            logger.info(String.format("Completed control loop in %s seconds.", elapsed.toSeconds()));

            try {
                // If we spent more time than 'allowed' during the control loop, set the interval to 0.
                Duration waitTimeBeforeNextLoop = interval.minus(elapsed).toMillis() >= 0 ? interval.minus(elapsed) : Duration.ZERO;
                logger.info(String.format("Waiting %s seconds before proceeding to next control iteration.",
                        waitTimeBeforeNextLoop.toSeconds()));
                Thread.sleep(waitTimeBeforeNextLoop.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void emitChargingProfiles(Map<String, ChargingProfile> profileMap) {
        if (profileMap.isEmpty()) return; // No need to emit anything.

        logger.info("Emitting ChargingProfiles to Charging Stations.");
        for (Map.Entry<String, ChargingProfile> entry : profileMap.entrySet()) {
            String csId = entry.getKey();
            ChargingProfile chargingProfile = entry.getValue();

            SetChargingProfileRequest payload = SetChargingProfileRequest.builder()
                    .withEvseId(0)
                    .withChargingProfile(chargingProfile)
                    .build();

            ICall<SetChargingProfileRequest> call = CallImpl.<SetChargingProfileRequest>newBuilder()
                    .asAction(OCPPMessageType.SetChargingProfileRequest.getAction())
                    .withMessageId(UUID.randomUUID().toString())
                    .withPayLoad(payload)
                    .build();

            try {
                IOCPPSession session = sessionManager.getSession(csId);
                Headers headers = Headers.emptyHeader();
                ICallResult<SetChargingProfileResponse> callResult = session.getChargingStation().sendSetChargingProfileRequest(headers, call);

                logger.info(String.format("Received response '%s' with payload %s",
                        SetChargingProfileResponse.class.getName(), callResult.getPayload().toString()));
            } catch (OCPPRequestException e) {
                logger.info(String.format("Error occurred while sending the request or receiving the response. %s",
                        e.getMessage()));
            }
        }
    }
}
