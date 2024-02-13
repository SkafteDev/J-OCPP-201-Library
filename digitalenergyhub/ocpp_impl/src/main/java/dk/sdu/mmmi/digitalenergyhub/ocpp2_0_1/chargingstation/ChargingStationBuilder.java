package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingProfile;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.schemas.json.ChargingStation;

public class ChargingStationBuilder {
    private final ChargingStationImpl csInstance = new ChargingStationImpl();
    private final ChargingStation chargingStationData = new ChargingStation();
    private ChargingProfile defaultChargingProfile = null;

    private ChargingStationBuilder() { }

    public static ChargingStationBuilder newBuilder() {
        return new ChargingStationBuilder();
    }

    public ChargingStationBuilder setSerialNumber(String serial) {
        chargingStationData.setSerialNumber(serial);
        return this;
    }

    public ChargingStationBuilder setModelName(String model) {
        chargingStationData.setModel(model);
        return this;
    }

    public ChargingStationBuilder setVendorName(String vendorName) {
        chargingStationData.setVendorName(vendorName);
        return this;
    }

    public ChargingStationBuilder setFirmwareVersion(String firmwareVersion) {
        chargingStationData.setFirmwareVersion(firmwareVersion);
        return this;
    }

    public ChargingStationBuilder withDefaultChargingProfile(ChargingProfile profile) {
        this.defaultChargingProfile = profile;
        return this;
    }

    public ChargingStationImpl build() {
        csInstance.addComponent(ChargingStation.class, chargingStationData);
        csInstance.addComponent(ChargingProfile.class, defaultChargingProfile);

        return csInstance;
    }
}
