package dk.sdu.mmmi.application.csms;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.managementsystem.IChargingStationManagementServer;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerConfig;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.BrokerContextLoader;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration.IBrokerContext;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.servers.managementsystem.ChargingStationManagementServerImpl;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class CsmsExampleApp {
    private static final Logger logger = Logger.getLogger(CsmsExampleApp.class.getName());

    private static final String quitToken = "q";

    public static void main(String[] args) {
        logger.info("Booting Charging Station Management System...");
        String csmsId = args[0];
        IChargingStationManagementServer server = boot(csmsId);
        logger.info("Booting complete.");

        server.serve();   // Handle incoming messages.
        server.startSmartChargingControlLoop(Duration.ofSeconds(15)); // Start the smart charging control loop.

        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }

    private static IChargingStationManagementServer boot(String csmsId) {
        URL resource = ClassLoader.getSystemResource("brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        BrokerConfig brokerConfig = brokerContext.getConfigFromCsmsId(csmsId);

        try {
            Options natsOptions = Options.builder()
                    .server(brokerConfig.getBrokerUrl())
                    .connectionName(String.format("CSMS %s %s", brokerConfig.getOperatorId(), brokerConfig.getCsmsId()))
                    .connectionTimeout(Duration.ofMinutes(2))
                    .connectionListener((connection, eventType) -> {
                        logger.info(String.format("NATS.io connection event: %s%n", eventType));
                    })
                    .build();

            Connection natsConnection = Nats.connect(natsOptions);
            IChargingStationManagementServer server = new ChargingStationManagementServerImpl(
                    brokerConfig.getOperatorId(),
                    brokerConfig.getCsmsId(),
                    natsConnection);

            return server;
        } catch (IOException | InterruptedException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to boot CSMS.", e);
        }
    }
}
