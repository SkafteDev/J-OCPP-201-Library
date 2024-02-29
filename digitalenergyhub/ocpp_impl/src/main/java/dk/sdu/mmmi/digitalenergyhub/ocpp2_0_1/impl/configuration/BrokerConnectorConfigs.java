package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.configuration;

import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.impl.routes.MessageRouteResolverImpl;

import java.util.List;
import java.util.Optional;

public class BrokerConnectorConfigs implements IBrokerConnectorConfigs {
    private List<BrokerConnectorConfig> brokerConnectorConfigs;

    // Getters and setters
    public List<BrokerConnectorConfig> getBrokerConnectorConfigs() {
        return brokerConnectorConfigs;
    }

    public void setBrokerConnectorConfigs(List<BrokerConnectorConfig> brokerConnectorConfigs) {
        this.brokerConnectorConfigs = brokerConnectorConfigs;
    }

    @Override
    public IMessageRouteResolver getChargingStationRouteResolver(String csId) {
        Optional<BrokerConnectorConfig> matchingEntry = brokerConnectorConfigs.stream()
                .filter(
                        brokerConnectorConfig -> brokerConnectorConfig.getChargingStationIds().contains(csId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationIdNotFoundException("The Charging Station Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return new MessageRouteResolverImpl(
                matchingEntry.get().getOperatorId(),
                matchingEntry.get().getCsmsId(),
                csId
        );
    }

    @Override
    public BrokerConnectorConfig getConfigFromCsId(String csId) {
        Optional<BrokerConnectorConfig> matchingEntry = brokerConnectorConfigs.stream()
                .filter(
                        brokerConnectorConfig -> brokerConnectorConfig.getChargingStationIds().contains(csId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationIdNotFoundException("The Charging Station Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return matchingEntry.get();
    }

    @Override
    public BrokerConnectorConfig getConfigFromCsmsId(String csmsId) {
        Optional<BrokerConnectorConfig> matchingEntry = brokerConnectorConfigs.stream()
                .filter(
                        brokerConnectorConfig -> brokerConnectorConfig.getCsmsId().equals(csmsId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationManagementSystemIdNotFoundException("The Charging Station Management Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return matchingEntry.get();
    }

    @Override
    public IMessageRouteResolver getCsmsRouteResolver(String csmsId) {
        Optional<BrokerConnectorConfig> matchingEntry = brokerConnectorConfigs.stream()
                .filter(
                        brokerConnectorConfig -> brokerConnectorConfig.getCsmsId().equals(csmsId))
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationManagementSystemIdNotFoundException("The Charging Station Management Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return new MessageRouteResolverImpl(
                matchingEntry.get().getOperatorId(),
                matchingEntry.get().getCsmsId(),
                "*"
        );
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
