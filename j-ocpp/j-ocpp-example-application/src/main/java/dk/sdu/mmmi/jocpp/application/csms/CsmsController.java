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

package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.Headers;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.exceptions.OCPPRequestException;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICall;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.api.ICallResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.rpcframework.impl.CallImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.schemas.json.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CsmsController {

    private static final Logger logger = LoggerFactory.getLogger(CsmsController.class.getName());

    private final ISessionManager sessionManager;

    public CsmsController(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
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
}
