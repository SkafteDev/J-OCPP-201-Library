package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serialization.callmessage.payloads;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.OCPPMessageType;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.utils.DateUtil;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.api.ICallMessage;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.deserializers.CallMessageDeserializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.impl.CallMessageImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.rpcframework.serializers.CallMessageSerializer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChargingProfileRequestDeserializerTest {

    @Test
    void unit_chargingProfile_deserializer_test() {
        ChargingProfile chargingProfile = getChargingProfile();

        SetChargingProfileRequest payload = SetChargingProfileRequest.builder()
                .withEvseId(0)
                .withChargingProfile(chargingProfile)
                .build();

        ICallMessage<SetChargingProfileRequest> expected = CallMessageImpl.<SetChargingProfileRequest>newBuilder()
                .asAction(OCPPMessageType.SetChargingProfileRequest.getValue())
                .withMessageId("xxxyyyzzz123")
                .withPayLoad(payload)
                .build();

        try {
            String jsonCall = "[2,\"xxxyyyzzz123\",\"SetChargingProfile\",{\"evseId\":0,\"chargingProfile\":{\"id\":1,\"stackLevel\":0,\"chargingProfilePurpose\":\"ChargingStationMaxProfile\",\"chargingProfileKind\":\"Absolute\",\"chargingSchedule\":[{\"id\":1,\"startSchedule\":\"2024-01-20T00:00:00Z\",\"chargingRateUnit\":\"W\",\"chargingSchedulePeriod\":[{\"startPeriod\":0,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":3600,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":7200,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":10800,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":14400,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":18000,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":21600,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":25200,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":28800,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":32400,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":36000,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":39600,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":43200,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":46800,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":50400,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":54000,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":57600,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":61200,\"limit\":0.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":64800,\"limit\":0.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":68400,\"limit\":0.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":72000,\"limit\":0.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":75600,\"limit\":0.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":79200,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1},{\"startPeriod\":82800,\"limit\":11000.0,\"numberPhases\":1,\"phaseToUse\":1}]}]}}]";
            ICallMessage<SetChargingProfileRequest> actual = CallMessageDeserializer.deserialize(jsonCall, SetChargingProfileRequest.class);

            assertEquals(expected.getMessageId(), actual.getMessageId());
            assertEquals(expected.getMessageTypeId(), actual.getMessageTypeId());
            assertEquals(expected.getAction(), actual.getAction());
            assertEquals(expected.getPayload(), actual.getPayload());

        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    private static ChargingProfile getChargingProfile() {
        return ChargingProfile.builder()
                .withId(1)
                .withStackLevel(0)
                .withChargingProfilePurpose(ChargingProfilePurposeEnum.CHARGING_STATION_MAX_PROFILE)
                .withChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE)
                .withChargingSchedule(
                        List.of(
                                ChargingSchedule.builder()
                                        .withId(1)
                                        .withStartSchedule(DateUtil.of(2024, 1, 20, 0, 0, 0))
                                        .withChargingRateUnit(ChargingRateUnitEnum.W)
                                        /*
                                         * From this point is the limit for each hour in the day
                                         */
                                        .withChargingSchedulePeriod(List.of(
                                                // From 00:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(0 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 01:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(1 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 02:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(2 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 03:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(3 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 04:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(4 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 05:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(5 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 06:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(6 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 07:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(7 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 08:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(8 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 09:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(9 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 10:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(10 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 11:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(11 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 12:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(12 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 13:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(13 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 14:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(14 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 15:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(15 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 16:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(16 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 17:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(17 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 18:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(18 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 19:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(19 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 20:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(20 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 21:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(21 * 60 * 60)
                                                        .withLimit(0d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 22:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(22 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build(),
                                                // From 23:00
                                                ChargingSchedulePeriod.builder()
                                                        .withStartPeriod(23 * 60 * 60)
                                                        .withLimit(11_000d)
                                                        .withNumberPhases(1)
                                                        .withPhaseToUse(1)
                                                        .build()
                                        )).build()
                        ))
                .build();
    }
}
