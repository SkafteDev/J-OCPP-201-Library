package dk.sdu.mmmi.jocpp.application.csms;

import java.time.Duration;

public interface IChargingStationManagementServer {

    void serve();

    void startSmartChargingControlLoop(Duration interval);
}
