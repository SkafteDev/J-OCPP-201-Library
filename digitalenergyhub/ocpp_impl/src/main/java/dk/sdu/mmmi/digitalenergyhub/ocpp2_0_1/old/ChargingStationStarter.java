package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.chargingstation.ChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.chargingstation.handlers.SetChargingProfileRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.old.messagetypes.OCPPMessageToSubjectMapping;

public class ChargingStationStarter {
    public static void main(String[] args) {
        ChargingStationServer cs = startChargingStation();
    }

    private static ChargingStationServer startChargingStation() {
        System.out.println("Starting Charging Station.");
        ChargingStationServer cs = new ChargingStationServer(
                "Clever",
                "Clever Central CSMS",
                "denmark_odense_m_draejebaenken_STA1",
                "nats://localhost:4222");

        cs.addRequestHandler(OCPPMessageToSubjectMapping.OCPPMessageType.SetChargingProfileRequest,
                new SetChargingProfileRequestHandler(cs));
        System.out.println("Charging Station Started.");

        return cs;
    }
}
