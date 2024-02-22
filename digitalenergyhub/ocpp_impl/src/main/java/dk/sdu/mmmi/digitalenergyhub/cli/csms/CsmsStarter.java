package dk.sdu.mmmi.digitalenergyhub.cli.csms;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.ChargingStationManagementServerImpl;

import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class CsmsStarter {
    private static final Logger logger = Logger.getLogger(CsmsStarter.class.getName());

    private static final String quitToken = "q";

    public static void main(String[] args) {
        logger.info("Booting Charging Station Management System...");
        ChargingStationManagementServerImpl server = boot();
        logger.info("Booting complete.");

        server.connect(); // Connect to broker.
        server.serve();   // Listen to incoming messages.
        server.startSmartChargingControlLoop(Duration.ofMinutes(1));

        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }

    private static ChargingStationManagementServerImpl boot() {
        ChargingStationManagementServerImpl server = new ChargingStationManagementServerImpl(
                "Clever",
                "Clever Central CSMS",
                "nats://localhost:4222");

        return server;
    }
}
