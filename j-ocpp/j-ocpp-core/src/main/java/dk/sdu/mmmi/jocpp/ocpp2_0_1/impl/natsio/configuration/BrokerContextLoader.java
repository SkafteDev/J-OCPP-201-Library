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

package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration;

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
