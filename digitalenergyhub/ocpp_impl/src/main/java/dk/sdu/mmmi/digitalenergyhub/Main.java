package dk.sdu.mmmi.digitalenergyhub;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.ChargingStationBuilder;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.ChargingStationImpl;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfile;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfileKindEnum;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfilePurposeEnum;

public class Main {
    public static void main(String[] args) {
        ChargingProfile chargingProfile = new ChargingProfile();
        chargingProfile.setId(1);
        chargingProfile.setStackLevel(0);
        chargingProfile.setChargingProfilePurpose(ChargingProfilePurposeEnum.CHARGING_STATION_MAX_PROFILE);
        chargingProfile.setChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE);
        //chargingProfile.setChargingSchedule();
        //chargingProfile.set
        ChargingProfile.builder()
                .withChargingProfileKind(ChargingProfileKindEnum.ABSOLUTE)
                .withChar




        ChargingStationImpl csImpl = ChargingStationBuilder.newBuilder()
                .setVendorName("Clever")
                .setModelName("SuperCharger 2000")
                .setFirmwareVersion("1.0.0")
                .withDefaultChargingProfile()
    }
}
