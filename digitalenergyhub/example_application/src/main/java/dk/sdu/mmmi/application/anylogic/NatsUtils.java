package dk.sdu.mmmi.application.anylogic;

import io.nats.client.Options;

import java.io.IOException;
import java.io.Serializable;

/**
 * NatsUtils
 */
public class NatsUtils implements Serializable {

    /**
     * This number is here for model snapshot storing purpose<br>
     * It needs to be changed when this class gets changed
     */
    private static final long serialVersionUID = 1L;

    private NatsUtils() { }

    public static io.nats.client.Connection createConnection(Options options) {
        try {
            return io.nats.client.Nats.connect(options);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
