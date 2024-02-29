package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

public class BrokerConnectorConfigsLoader {

    private BrokerConnectorConfigsLoader() {}

    public static BrokerConnectorConfigs loadBrokerConnectionConfigs(String pathName) {
        Yaml yaml = new Yaml();

        try(FileInputStream inputStream = new FileInputStream(pathName)) {
            return yaml.loadAs(inputStream, BrokerConnectorConfigs.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
