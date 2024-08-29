package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.HandshakeResult;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IHandshakeResponse;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.OcppVersion;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "handshakeResult",
        "ocppVersion",
        "endpoint",
        "reason"
})
public class HandshakeResponseImpl implements IHandshakeResponse {

    @JsonProperty("handshakeResult")
    private HandshakeResult handshakeResult;

    @JsonProperty("ocppVersion")
    private OcppVersion ocppVersion;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("reason")
    private String reason;

    // Empty constructor is required for serialization/deserialization
    public HandshakeResponseImpl() {}

    @JsonProperty("endpoint")
    @Override
    public String getEndPoint() {
        return endpoint;
    }

    @JsonProperty("handshakeResult")
    @Override
    public HandshakeResult getHandshakeResult() {
        return handshakeResult;
    }

    @JsonProperty("ocppVersion")
    @Override
    public OcppVersion getOcppVersion() {
        return ocppVersion;
    }

    @JsonProperty("reason")
    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandshakeResponseImpl)) return false;
        HandshakeResponseImpl that = (HandshakeResponseImpl) o;
        return Objects.equals(handshakeResult, that.handshakeResult) && Objects.equals(ocppVersion, that.ocppVersion) && Objects.equals(endpoint, that.endpoint) && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handshakeResult, ocppVersion, endpoint, reason);
    }

    public static final class HandshakeResponseImplBuilder {
        private String reason;
        private HandshakeResult handshakeResult;
        private OcppVersion ocppVersion;
        private String endpoint;

        private HandshakeResponseImplBuilder() {
        }

        public static HandshakeResponseImplBuilder aHandshakeResponseImpl() {
            return new HandshakeResponseImplBuilder();
        }

        public HandshakeResponseImplBuilder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public HandshakeResponseImplBuilder withHandshakeResult(HandshakeResult handshakeResult) {
            this.handshakeResult = handshakeResult;
            return this;
        }

        public HandshakeResponseImplBuilder withOcppVersion(OcppVersion ocppVersion) {
            this.ocppVersion = ocppVersion;
            return this;
        }

        public HandshakeResponseImplBuilder withEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public HandshakeResponseImpl build() {
            HandshakeResponseImpl handshakeResponseImpl = new HandshakeResponseImpl();
            handshakeResponseImpl.handshakeResult = this.handshakeResult;
            handshakeResponseImpl.ocppVersion = this.ocppVersion;
            handshakeResponseImpl.endpoint = this.endpoint;
            handshakeResponseImpl.reason = this.reason;
            return handshakeResponseImpl;
        }
    }
}
