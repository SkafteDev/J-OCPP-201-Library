package dk.sdu.mmmi.application.anylogic;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Options;
import io.nats.client.Subscription;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class AnyLogicServer {
    public static void main(String[] args) throws IOException {

        InputStream resourceStream = AnyLogicServer.class.getClassLoader().getResourceAsStream("anylogic.properties");
        Properties properties = new Properties();
        properties.load(resourceStream);
        String natsUrl = properties.getProperty("anylogic.brokerurl");
        String anyLogicExecutable = properties.getProperty("anylogic.executable");
        String anyLogicModel = properties.getProperty("anylogic.model");
        String anylogicExperiment = properties.getProperty("anylogic.experiment");
        String anylogicInstanceId = properties.getProperty("anylogic.instanceid");

        Connection nc = NatsUtils.createConnection(Options.builder()
                .server(natsUrl)
                .build()
        );


        try {
            System.out.println("AnyLogic Server started.");

            String subject = "anylogic.{instanceid}.commands.open".replace("{instanceid}", anylogicInstanceId);
            Subscription sub = nc.subscribe(subject);
            Message msg = sub.nextMessage(Duration.ZERO);

            new AnyLogicCLI(anyLogicExecutable)
                    .run(anyLogicModel, anylogicExperiment);

            nc.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
