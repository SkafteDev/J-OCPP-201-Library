package dk.sdu.mmmi.digitalenergyhub.application.csms;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.ChargingStationManagementServerImpl;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class ExampleApplication {
    private static final Logger logger = Logger.getLogger(ExampleApplication.class.getName());

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
        String operatorId = "Clever";
        String csmsId = "Clever Central CSMS";
        String natsConnectionURL = "nats://localhost:4222";

        try {
            Options natsOptions = Options.builder()
                    .server(natsConnectionURL)
                    .connectionName(String.format("CSMS %s %s", operatorId, csmsId))
                    .connectionTimeout(Duration.ofMinutes(2))
                    .connectionListener((connection, eventType) -> {
                        logger.info(String.format("NATS.io connection event: %s%n", eventType));
                    })
                    .build();

            Connection natsConnection = Nats.connect(natsOptions);
            ChargingStationManagementServerImpl server = new ChargingStationManagementServerImpl(
                    "Clever",
                    "Clever Central CSMS",
                    natsConnection);

            return server;
        } catch (IOException | InterruptedException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to boot CSMS.", e);
        }
    }
}
