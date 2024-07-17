package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.configuration;

import java.util.List;

public class BrokerConfig {
    private String brokerUrl;
    private String operatorId;
    private String csmsId;
    private List<String> chargingStationIds;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

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
