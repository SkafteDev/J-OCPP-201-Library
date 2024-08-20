package dk.sdu.mmmi.jocpp.application.csms;

import java.time.Duration;

public interface ICsmsServer {

    void serve();

    void startSmartChargingControlLoop(Duration interval);
}
