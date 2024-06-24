package dk.sdu.mmmi.application.natskeyvalue;

import io.nats.client.*;
import io.nats.client.api.KeyValueConfiguration;
import io.nats.client.api.KeyValueEntry;
import io.nats.client.api.KeyValueStatus;
import io.nats.client.api.StorageType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This is an example on how to use the NATS.io key/value stored.
 * JetStream must be enabled on the NATS.io server.
 * Example is based on this link: https://github.com/nats-io/nats.java/blob/main/src/examples/java/io/nats/examples/jetstream/NatsKeyValueFull.java
 */
public class KeyValueExample {
    public static void main(String[] args) throws IOException, InterruptedException, JetStreamApiException {

        // Connection options for connecting to NATS.io.
        Options natsOptions = Options.builder()
                .server("nats://localhost:4222")
                .build();

        // (1) Connect to NATS.io
        try (Connection nc = Nats.connect(natsOptions)) {
            // (2) Get the key/value management context
            KeyValueManagement keyValueManagement = nc.keyValueManagement();

            // (3) Create the bucket
            String bucketName = "simulation1"; // The name of the distributed "map". In NATS.io it's called a bucket.
            KeyValueConfiguration kvConfiguration = KeyValueConfiguration.builder()
                    .name(bucketName)
                    .maxHistoryPerKey(10) // We can read historic values from the key/value store.
                    .storageType(StorageType.File)
                    .description("A distributed key/value store for simulation 1.")
                    .build();
            KeyValueStatus keyValueStatus = keyValueManagement.create(kvConfiguration); // This line creates the bucket.
            System.out.println(keyValueStatus);

            // (4) Retrieve the distributed "map".
            KeyValue kvs = nc.keyValue(bucketName);

            // (5) Put some values in the "map".
            String myKey = "transformer.1.load";
            double transformerLoad = 300000.0; // The value [kW]
            kvs.put(myKey, transformerLoad);
            System.out.printf("Put key: %s, value: %s%n", myKey, transformerLoad);

            // (6) Retrieve the values for a key.
            KeyValueEntry keyValueEntry = kvs.get(myKey); // The key/value pair.
            byte[] byteValue = keyValueEntry.getValue(); // Nats.io stores the value in bytes.
            String stringValue = new String(byteValue, StandardCharsets.UTF_8); // Parse bytes to a String representation.
            double value = Double.parseDouble(stringValue); // Parse the string to a Double (just as the value we stored).
            System.out.printf("Get key: %s, value: %s%n", myKey, value);

        }
    }
}
