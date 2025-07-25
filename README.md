# Java-based OCPP 2.0.1 Framework

This repository contains a Java implementation of the Open Charge Point Protocol (OCPP) 2.0.1.  The project follows a broker based communication architecture and the provided examples use [NATS.io](https://nats.io) for message exchange.  The library itself is **transport agnostic** and can be integrated with any underlying transport such as MQTT, WebSocket or other message brokers.

## Repository structure

- **`j-ocpp-schemas`** – JSON schemas for all OCPP 2.0.1 messages.  A Maven plug‑in generates the Java POJOs used throughout the project.
- **`j-ocpp-core`** – Contains the RPC framework, message serializers/deserializers and abstract session/endpoint APIs.  NATS.io based implementations are provided as one possible transport layer.
- **`j-ocpp-example-application`** – Example programs showing how to build a Charging Station (CS) and a Charging Station Management System (CSMS).  Both offline/in memory and online/NATS variants are included.
- **`infrastructure`** – Docker compose setup used for running a local NATS server together with optional services such as InfluxDB and Telegraf.

## Prerequisites

- Java 11 or newer
- Maven 3.8+
- Docker (required for running the NATS/influx stack)

## Building the project

From the `j-ocpp` folder run:

```bash
mvn package
```

This builds all modules and produces the example application JAR.

## Running the examples

### Offline example

The `ChargingStationOfflineDemo` demonstrates a CS and CSMS communicating via an in memory session – no external broker required.  After building the project execute:

```bash
java -cp j-ocpp-example-application/target/j-ocpp-example-application-2.1.1.jar \
    dk.sdu.mmmi.jocpp.application.chargingstation.ChargingStationOfflineDemo
```

### Online example using NATS.io

Start the infrastructure defined in `infrastructure/compose.yml`:

```bash
docker compose -f infrastructure/compose.yml up
```

This starts a NATS server as shown in the compose file:

```yaml
middleware_messaging:
  image: nats:2.9.25
  container_name: nats-server
  command: -c /container/configs/nats.conf
  ports:
    - "1883:1883"
    - "4222:4222"
    - "6222:6222"
    - "8222:8222"
    - "8080:8080"
```

Once the broker is running you can launch the CSMS and CS demos in separate terminals:

```bash
# CSMS instance
java -cp j-ocpp-example-application/target/j-ocpp-example-application-2.1.1.jar \
    dk.sdu.mmmi.jocpp.application.csms.CSMSOverNatsDemo "EWII CSMS"

# Charging Station
java -cp j-ocpp-example-application/target/j-ocpp-example-application-2.1.1.jar \
    dk.sdu.mmmi.jocpp.application.chargingstation.ChargingStationOverNatsDemo
```

## Constructing and processing messages

Messages are constructed using the generated builders.  The `CsController` builds and sends a `BootNotificationRequest` as shown below:

```java
BootNotificationRequest bootNotificationRequest = BootNotificationRequest.builder()
        .withChargingStation(ChargingStation.builder()
                .withVendorName("ABB")
                .withSerialNumber("f8125503-8d0f-467f-abad-b830ca6782e2")
                .withModel("ABB TAC-W11-G5-R-0")
                .withFirmwareVersion("3.1.3")
                .build())
        .withReason(BootReasonEnum.POWER_UP)
        .build();

ICall<BootNotificationRequest> bootRequest = CallImpl.<BootNotificationRequest>newBuilder()
        .withMessageId(UUID.randomUUID().toString())
        .asAction(OCPPMessageType.BootNotificationRequest.getAction())
        .withPayLoad(bootNotificationRequest)
        .build();

ICallResult<BootNotificationResponse> response = ocppSession.getCsms()
        .sendBootNotificationRequest(Headers.emptyHeader(), bootRequest);
```

On the CSMS side an endpoint processes the request and returns a response:

```java
public ICallResult<BootNotificationResponse> sendBootNotificationRequest(Headers headers,
        ICall<BootNotificationRequest> req) {
    BootNotificationResponse bootNotificationResponse = BootNotificationResponse.builder()
            .withStatus(RegistrationStatusEnum.ACCEPTED)
            .withCurrentTime(ZonedDateTime.now())
            .withInterval((int) Duration.ofMinutes(2).toSeconds())
            .build();

    return CallResultImpl.<BootNotificationResponse>newBuilder()
            .withMessageId(req.getMessageId())
            .withPayLoad(bootNotificationResponse)
            .build();
}
```

Both the CS and CSMS implementations only depend on the abstract session APIs.  This allows other transport technologies such as MQTT or WebSockets to be integrated simply by providing compatible session and endpoint implementations.

---

For additional details please refer to the source code in the example application and the individual modules.
