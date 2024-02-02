# AnyLogic RealTime Demo
This project demonstrates that AnyLogic real-time execution.

* AnyLogic was configured as described in ‘16.3 Virtual and real-time execution modes’.
    1. Decide upon the model time e.g. second, minute, hourly, weekly, monthly, yearly.
    2. Set AnyLogic to execute x model time units pr. 1 real second. The x variable depends upon the model time set in AnyLogic. This is important to set right.

If the model is executed faster than configured then the model is simply paused until it matches the real-time setting.

If the model executes slower than configured, AnyLogic will try and calculate as fast as possible until it finds opportunity to keep the real-time execution.


Including in this demo is a Maven-based Java project that defines the messages exchange between the Java program and the AnyLogic simulation. Communication is based on NATS.io and messages are serialized via Protobuf.

## Sample output:

### AnyLogic console window
```sh
Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "a5d0233a-7564-4c67-b199-47a242da5f5a"
  timestamp {
    seconds: 1706895391
    nanos: 126000000
  }
}
json_payload: "{ diff: -0.01268449}"

Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "89e27645-2f6c-4e44-a9f0-efee8cb8856b"
  timestamp {
    seconds: 1706895392
    nanos: 390000000
  }
}
json_payload: "{ diff: -0.03902945}"

Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "9c9ac6fc-d8a3-4a1a-a204-3560b15d91eb"
  timestamp {
    seconds: 1706895393
    nanos: 562000000
  }
}
json_payload: "{ diff: -0.05626764}"

Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "71edf8f2-1b49-41ea-a687-9fe294502da7"
  timestamp {
    seconds: 1706895394
    nanos: 751000000
  }
}
json_payload: "{ diff: -0.07511102}"

Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "feaf91bc-b71f-4189-a19e-d59e79585441"
  timestamp {
    seconds: 1706895395
    nanos: 940000000
  }
}
json_payload: "{ diff: -0.0940123}"

Published: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "9d186b55-a2cf-4cf4-81da-3b275ec5d702"
  timestamp {
    seconds: 1706895397
    nanos: 126000000
  }
}
json_payload: "{ diff: -0.01263426}"
```

### Java console window
```sh
Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "a5d0233a-7564-4c67-b199-47a242da5f5a"
  timestamp {
    seconds: 1706895391
    nanos: 126000000
  }
}
json_payload: "{ diff: -0.01268449}"

Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "89e27645-2f6c-4e44-a9f0-efee8cb8856b"
  timestamp {
    seconds: 1706895392
    nanos: 390000000
  }
}
json_payload: "{ diff: -0.03902945}"

Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "9c9ac6fc-d8a3-4a1a-a204-3560b15d91eb"
  timestamp {
    seconds: 1706895393
    nanos: 562000000
  }
}
json_payload: "{ diff: -0.05626764}"

Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "71edf8f2-1b49-41ea-a687-9fe294502da7"
  timestamp {
    seconds: 1706895394
    nanos: 751000000
  }
}
json_payload: "{ diff: -0.07511102}"

Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "feaf91bc-b71f-4189-a19e-d59e79585441"
  timestamp {
    seconds: 1706895395
    nanos: 940000000
  }
}
json_payload: "{ diff: -0.0940123}"

Received msg on subject my_topic
Raw: meta {
  sender_id: "AnyLogic simulation model"
  message_id: "9d186b55-a2cf-4cf4-81da-3b275ec5d702"
  timestamp {
    seconds: 1706895397
    nanos: 126000000
  }
}
json_payload: "{ diff: -0.01263426}"
```