/*
 * Copyright (c) 2024 SDU Center for Energy Informatics, University of Southern Denmark.
 * All rights reserved.
 *
 * Author: Christian Skafte Beck Clausen (csbc@mmmi.sdu.dk)
 *
 *  This code is proprietary and confidential.
 *  Unauthorized copying of this file, via any medium, is strictly prohibited unless permission
 *  is granted by SDU Center for Energy Informatics, University of Southern Denmark.
 */

package dk.sdu.mmmi.jocpp.application.csms;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.ISessionManager;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.CsmsNatsSkeleton;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services.SessionManagerImpl;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerConfig;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration.BrokerContextLoader;
import io.nats.client.Options;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;

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
public class CSMSOverNatsDemo {
    private static final Logger logger = LoggerFactory.getLogger(CSMSOverNatsDemo.class.getName());

    private static final String quitToken = "q";

    public static void main(String[] args) {
        logger.info("Booting Charging Station Management System...");
        String csmsId = args[0];
        CsmsNatsSkeleton server = boot(csmsId);
        logger.info("Booting complete.");

        server.serve();   // Handle incoming messages.

        System.out.printf("%nPress '%s' to exit.%n", quitToken);
        String readLine = null;
        Scanner s = new Scanner(System.in);
        while (!Objects.equals(readLine = s.nextLine(), quitToken)) {
            System.out.println("Unrecognized command: " + readLine);
        }

        System.exit(0);
    }

    private static CsmsNatsSkeleton boot(String csmsId) {
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

        ISessionManager sessionManager = new SessionManagerImpl();
        sessionManager.addListener(session -> logger.info(String.format("New session established: %s", session.getSessionInfo())));
        CsmsEndpoint csmsEndpoint = new CsmsEndpoint(csmsId, sessionManager);
        CsmsController csmsController = new CsmsController(sessionManager);
        new Thread(() -> {
            csmsController.startSmartChargingControlLoop(Duration.ofSeconds(15));
        }).start();

        CsmsNatsSkeleton server = new CsmsNatsSkeleton(brokerConfig, natsOptions, csmsEndpoint, sessionManager);

        return server;
    }
}
