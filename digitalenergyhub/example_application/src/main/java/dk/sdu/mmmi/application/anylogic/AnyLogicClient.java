package dk.sdu.mmmi.application.anylogic;

import io.nats.client.Connection;
import io.nats.client.Options;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class AnyLogicClient {

    private final String anylogicInstanceId;
    private Connection natsConnection;

    public AnyLogicClient() throws IOException {

        InputStream resourceStream = AnyLogicClient.class.getClassLoader().getResourceAsStream("anylogic.properties");
        Properties properties = new Properties();
        properties.load(resourceStream);
        String natsUrl = properties.getProperty("anylogic.brokerurl");
        this.anylogicInstanceId = properties.getProperty("anylogic.instanceid");

        this.natsConnection = NatsUtils.createConnection(Options.builder()
                .server(natsUrl)
                .build()
        );
    }

    public void openExperiment() {
        try {
            String subject = "anylogic.{instanceid}.commands.open".replace("{instanceid}",
                    anylogicInstanceId);

            natsConnection.publish(subject, "".getBytes(StandardCharsets.UTF_8));

            // Make sure the message goes through before we close
            natsConnection.flush(Duration.ZERO);
            natsConnection.close();
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instructs the AnyLogic server to run the experiment.
     * @param startDate e.g. 01-03-2024
     * @param stopDate e.g. 31-03-2024
     */
    public void runExperiment(String startDate, String stopDate) {
        try {
            String jsonPayload = format("{" +
                            "\"startDate\": \"%s\"," +
                            "\"stopDate\": \"%s\"" +
                            "}",

                    // JSON parameters
                    startDate, stopDate
            );

            natsConnection.publish("anylogic.commands.start", jsonPayload.getBytes(StandardCharsets.UTF_8));

            // Make sure the message goes through before we close
            natsConnection.flush(Duration.ZERO);
            natsConnection.close();
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        AnyLogicClient proxy = new AnyLogicClient();
        proxy.openExperiment();
        proxy.runExperiment("01-03-2024","31-03-2024");
    }
}
