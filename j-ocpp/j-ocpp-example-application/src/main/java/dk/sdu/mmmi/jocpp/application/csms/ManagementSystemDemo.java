package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration.BrokerContextLoader;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This example demonstrates the use case of:
 *
 * 1) Running an implementation of a CSMS.
 *
 *    The example implementation is capable of:
 *
 *      Receiving requests from a charging station:
 *      - BootNotificationRequest
 *      - Heartbeat
 *      - StatusNotification
 *
 *      Sending requests to a charging station:
 *      - SetChargingProfileRequest
 */
public class ManagementSystemDemo {
    private static final Logger logger = Logger.getLogger(ManagementSystemDemo.class.getName());

    private static final String quitToken = "q";

    public static void main(String[] args) {
        logger.info("Booting Charging Station Management System...");
        String csmsId = args[0];
        ICsmsServer server = boot(csmsId);
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

    private static ICsmsServer boot(String csmsId) {
        URL resource = ClassLoader.getSystemResource("brokerContext.yml");
        IBrokerContext brokerContext = BrokerContextLoader.fromYAML(resource.getPath());
        BrokerConfig brokerConfig = brokerContext.getConfigFromCsmsId(csmsId);

        Options natsOptions = Options.builder()
                .server(brokerConfig.getBrokerUrl())
                .connectionName(String.format("CSMS %s %s", brokerConfig.getOperatorId(), brokerConfig.getCsmsId()))
                .connectionTimeout(Duration.ofMinutes(2))
                .connectionListener((connection, eventType) -> {
                    logger.info(String.format("NATS.io connection event: %s%n", eventType));
                })
                .build();

        ICsmsServer server = new CsmsNatsSkeleton(brokerConfig, natsOptions);

        return server;
    }
}
