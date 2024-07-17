package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

public class BrokerContextLoader {

    private BrokerContextLoader() {}

    public static IBrokerContext fromYAML(String pathName) {
        Yaml yaml = new Yaml();

        try(FileInputStream inputStream = new FileInputStream(pathName)) {
            return yaml.loadAs(inputStream, BrokerContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
