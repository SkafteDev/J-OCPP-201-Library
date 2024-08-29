package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.configuration;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.configuration.IBrokerContext;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.routes.IMessageRouteResolver;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.natsio.NatsMessageRouteResolver;

import java.util.List;
import java.util.Optional;

public class BrokerContext implements IBrokerContext {
    private List<BrokerConfig> brokerContext;

    // Getters and setters
    public List<BrokerConfig> getBrokerContext() {
        return brokerContext;
    }

    public void setBrokerContext(List<BrokerConfig> brokerConfigs) {
        this.brokerContext = brokerConfigs;
    }

    @Override
    public IMessageRouteResolver getChargingStationRouteResolver(String csId) {
        Optional<BrokerConfig> matchingEntry = brokerContext.stream()
                .filter(
                        brokerConfig -> brokerConfig.getChargingStationIds().contains(csId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationIdNotFoundException("The Charging Station Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return new NatsMessageRouteResolver(
                matchingEntry.get().getOperatorId(),
                matchingEntry.get().getCsmsId(),
                csId
        );
    }

    @Override
    public BrokerConfig getConfigFromCsId(String csId) {
        Optional<BrokerConfig> matchingEntry = brokerContext.stream()
                .filter(
                        brokerConfig -> brokerConfig.getChargingStationIds().contains(csId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationIdNotFoundException("The Charging Station Id was not found within the file. Make sure to provide a valid id contained within the file.");
        }

        return matchingEntry.get();
    }

    @Override
    public BrokerConfig getConfigFromCsmsId(String csmsId) {
        Optional<BrokerConfig> matchingEntry = brokerContext.stream()
                .filter(
                        brokerConfig -> brokerConfig.getCsmsId().equals(csmsId)
                )
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationManagementSystemIdNotFoundException(String.format("The Charging Station Management Id csmsId=%s was not found within the file. Make sure to provide a valid id contained within the file.", csmsId));
        }

        return matchingEntry.get();
    }

    @Override
    public IMessageRouteResolver getCsmsRouteResolver(String csmsId) {
        Optional<BrokerConfig> matchingEntry = brokerContext.stream()
                .filter(
                        brokerConfig -> brokerConfig.getCsmsId().equals(csmsId))
                .findFirst();

        if (matchingEntry.isEmpty()) {
            throw new ChargingStationManagementSystemIdNotFoundException(String.format("The Charging Station Management Id csmsId=%s was not found within the file. Make sure to provide a valid id contained within the file.", csmsId));
        }

        return new NatsMessageRouteResolver(
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
