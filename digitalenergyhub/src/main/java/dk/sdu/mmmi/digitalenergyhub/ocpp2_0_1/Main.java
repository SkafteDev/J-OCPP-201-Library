package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.ChargingStationServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.chargingstation.handlers.SetChargingProfileRequestHandler;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.managementsystem.ChargingStationManagementSystem;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes.OCPPMessageToSubjectMapping;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChargingStationManagementSystem csms = startCSMS();

        ChargingStationServer cs = new ChargingStationServer("Clever", "Clever Central CSMS",
                "denmark_odense_m_draejebaenken_STA1", "nats://localhost:4222");
        cs.addRequestHandler(OCPPMessageToSubjectMapping.OCPPMessageType.SetChargingProfileRequest,
                new SetChargingProfileRequestHandler(cs));

        System.out.println("\nPress 'q' to exit.");
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), "q")) {
            csms.calculateAndPublishNewChargingSchedules(cs.getCsId());
        }

        System.exit(0);
    }

    private static ChargingStationManagementSystem startCSMS() {
        System.out.println("Starting Charging Station Management System.");

        ChargingStationManagementSystem csms = new ChargingStationManagementSystem(
                "Clever",
                "Clever Central CSMS",
                "nats://localhost:4222");

        csms.addChargingStationProxy("denmark_odense_m_draejebaenken_STA1");

        System.out.println("Charging Station Management System started.");

        return csms;
    }
}
