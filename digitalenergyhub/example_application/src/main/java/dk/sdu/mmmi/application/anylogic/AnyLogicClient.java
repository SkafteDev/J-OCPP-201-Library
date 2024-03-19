package dk.sdu.mmmi.application.anylogic;

import io.nats.client.Options;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class AnyLogicClient {
    public static void main(String[] args) {
        io.nats.client.Connection nc = NatsUtils.createConnection(Options.builder()
                .server("nats://localhost:4222")
                .build()
        );

        try {
            String jsonPayload = format("{" +
                    "\"startDate\": \"%s\"," +
                    "\"stopDate\": \"%s\"" +
                    "}",

                    // JSON parameters
                    "01-03-2024","31-03-2024"
                    );

            nc.publish("anylogic.commands.start", jsonPayload.getBytes(StandardCharsets.UTF_8));

            // Make sure the message goes through before we close
            nc.flush(Duration.ZERO);
            nc.close();
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
