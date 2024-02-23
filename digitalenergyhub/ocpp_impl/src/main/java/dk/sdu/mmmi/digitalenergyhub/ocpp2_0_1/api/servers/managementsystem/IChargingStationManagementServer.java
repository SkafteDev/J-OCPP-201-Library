package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.api.servers.managementsystem;

import java.time.Duration;

public interface IChargingStationManagementServer {
    void connect();

    void serve();

    void startSmartChargingControlLoop(Duration interval);
}
