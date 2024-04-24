# Java-based OCPP 2.0.1 Framework
This project contains an OCPP 2.0.1 framework that uses a broker-based communication architecture via JSON/NATS.io. This is in contrast with the OCPP 2.0.1 standard that uses JSON/Websocket.

The Java framework contains:

* **The OCPP 2.0.1 RPC framework** with the three types of calls ***(i) CALL***, ***(ii) CALLRESULT***, and ***(iii) CALLERROR***.

* **OCPP 2.0.1 message payload types** (e.g. BootNotificationRequest, StatusNotificationRequest, SetChargingProfileRequest, etc.). Message payload types are embedded within the primary RPC call types. These classes were auto-generated from the OCPP 2.0.1 JSON schema.

* **Serializers/deserializers** for generic CALL, CALLRESULT and CALLERROR types. These types can be serialized from POJO to JSON and vice-versa with deserialization.

* **Proxy interfaces** for both CSMS and CS that define the calls that may be invoked from a Charging Station -> Charging Station Management System and vice versa.

* **A dispatch and request handler implementation ontop of NATS.io** to register/unregister how to dispatch inbound messages (requests) and generate outbound messages (responses).

# More info
For more info, see the repository wiki pages.