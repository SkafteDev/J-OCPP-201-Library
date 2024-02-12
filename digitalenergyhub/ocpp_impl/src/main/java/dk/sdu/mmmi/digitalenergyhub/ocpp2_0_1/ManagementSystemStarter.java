package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.managementsystem.ChargingStationManagementSystem;

import java.util.Objects;
import java.util.Scanner;

public class ManagementSystemStarter {
    public static void main(String[] args) {
        ChargingStationManagementSystem csms = startCSMS();

        System.out.println("\nPress 'q' to exit.");
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), "q")) {
            csms.calculateAndPublishNewChargingSchedules("denmark_odense_m_draejebaenken_sta1");
        }

        System.exit(0);
    }

    private static ChargingStationManagementSystem startCSMS() {
        System.out.println("Starting Charging Station Management System.");

        ChargingStationManagementSystem csms = new ChargingStationManagementSystem(
                "Clever",
                "Clever Central CSMS",
                "nats://localhost:4222");

        csms.addChargingStation("denmark_odense_m_draejebaenken_STA1");

        System.out.println("Charging Station Management System started.");

        return csms;
    }
}
