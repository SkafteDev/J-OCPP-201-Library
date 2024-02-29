package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MessageRouteResolverFactory {

    private MessageRouteResolverFactory() {}

    public static IMessageRouteResolver chargingStationRoutesFromYaml(String pathName, String csId) {
        RootObject rootObject = getRootObject(pathName);

        Optional<Entry> matchingEntry = rootObject.entries.stream().filter(entry -> entry.chargingStationIds.contains(csId)).findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationIdNotFoundException("The Charging Station Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return new MessageRouteResolverImpl(
                matchingEntry.get().operatorId,
                matchingEntry.get().csmsId,
                csId
        );
    }

    public static IMessageRouteResolver csmsRoutesFromYaml(String pathName, String csmsId) {
        RootObject rootObject = getRootObject(pathName);

        Optional<Entry> matchingEntry = rootObject.entries.stream().filter(entry -> entry.csmsId.equals(csmsId)).findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationManagementSystemIdNotFoundException("The Charging Station Management Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return new MessageRouteResolverImpl(
                matchingEntry.get().operatorId,
                matchingEntry.get().csmsId,
                "*"
        );
    }

    private static RootObject getRootObject(String pathName) {
        Yaml yaml = new Yaml();

        try(FileInputStream inputStream = new FileInputStream(pathName)) {
            return yaml.loadAs(inputStream, RootObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static class RootObject {
        private List<Entry> entries;

        // Getters and setters
        public List<Entry> getEntries() {
            return entries;
        }

        public void setEntries(List<Entry> entries) {
            this.entries = entries;
        }
    }

    public static class Entry {
        private String operatorId;
        private String csmsId;
        private List<String> chargingStationIds;

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getCsmsId() {
            return csmsId;
        }

        public void setCsmsId(String csmsId) {
            this.csmsId = csmsId;
        }

        public List<String> getChargingStationIds() {
            return chargingStationIds;
        }

        public void setChargingStationIds(List<String> chargingStationIds) {
            this.chargingStationIds = chargingStationIds;
        }
    }

    public static class ChargingStationIdNotFoundException extends RuntimeException {
        public ChargingStationIdNotFoundException(String msg) {
            super(msg);
        }

        public ChargingStationIdNotFoundException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class ChargingStationManagementSystemIdNotFoundException extends RuntimeException {
        public ChargingStationManagementSystemIdNotFoundException(String msg) {
            super(msg);
        }

        public ChargingStationManagementSystemIdNotFoundException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
