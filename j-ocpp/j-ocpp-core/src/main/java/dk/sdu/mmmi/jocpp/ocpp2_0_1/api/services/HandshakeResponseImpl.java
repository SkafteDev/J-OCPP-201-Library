package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "handshakeResult",
        "endpoint",
        "description"
})
public class HandshakeResponseImpl implements ICsmsService.HandshakeResponse {

    @JsonProperty("handshakeResult")
    private String handshakeResult;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("description")
    private String description;

    // Empty constructor is required for serialization/deserialization
    public HandshakeResponseImpl() {}

    @JsonProperty("endpoint")
    @Override
    public String getEndPoint() {
        return null;
    }

    @JsonProperty("handshakeResult")
    @Override
    public String getHandshakeResult() {
        return null;
    }

    @JsonProperty("description")
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandshakeResponseImpl)) return false;
        HandshakeResponseImpl that = (HandshakeResponseImpl) o;
        return Objects.equals(handshakeResult, that.handshakeResult) && Objects.equals(endpoint, that.endpoint) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handshakeResult, endpoint, description);
    }

    @Override
    public String toString() {
        return "HandshakeResponseImpl{" +
                "handshakeResult='" + handshakeResult + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static final class HandshakeResponseImplBuilder {
        private String handshakeResult;
        private String endpoint;
        private String description;

        private HandshakeResponseImplBuilder() {
        }

        public static HandshakeResponseImplBuilder newHandshakeResponseImpl() {
            return new HandshakeResponseImplBuilder();
        }

        public HandshakeResponseImplBuilder withHandshakeResult(String handshakeResult) {
            this.handshakeResult = handshakeResult;
            return this;
        }

        public HandshakeResponseImplBuilder withEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public HandshakeResponseImplBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public HandshakeResponseImpl build() {
            HandshakeResponseImpl handshakeResponseImpl = new HandshakeResponseImpl();
            handshakeResponseImpl.handshakeResult = this.handshakeResult;
            handshakeResponseImpl.description = this.description;
            handshakeResponseImpl.endpoint = this.endpoint;
            return handshakeResponseImpl;
        }
    }
}
