package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identity",
        "ocppVersion"
})
public class HandshakeRequestImpl implements ICsmsService.HandshakeRequest {

    @JsonProperty("identity")
    private String identity;

    @JsonProperty("ocppVersion")
    private String ocppVersion;

    @JsonProperty("identity")
    @Override
    public String getIdentity() {
        return identity;
    }

    @JsonProperty("ocppVersion")
    @Override
    public String getOcppVersion() {
        return ocppVersion;
    }

    // Empty constructor is required for serialization/deserialization
    public HandshakeRequestImpl() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandshakeRequestImpl)) return false;
        HandshakeRequestImpl that = (HandshakeRequestImpl) o;
        return Objects.equals(identity, that.identity) && Objects.equals(ocppVersion, that.ocppVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, ocppVersion);
    }

    @Override
    public String toString() {
        return "HandshakeRequestImpl{" +
                "identity='" + identity + '\'' +
                ", ocppVersion='" + ocppVersion + '\'' +
                '}';
    }

    public static final class HandshakeInfoImplBuilder {
        private String url;
        private String identity;
        private String ocppVersion;

        private HandshakeInfoImplBuilder() {
        }

        public static HandshakeInfoImplBuilder newBuilder() {
            return new HandshakeInfoImplBuilder();
        }

        public HandshakeInfoImplBuilder withIdentity(String identity) {
            this.identity = identity;
            return this;
        }

        public HandshakeInfoImplBuilder withOcppVersion(String ocppVersion) {
            this.ocppVersion = ocppVersion;
            return this;
        }

        public HandshakeRequestImpl build() {
            HandshakeRequestImpl handshakeInfoImpl = new HandshakeRequestImpl();
            handshakeInfoImpl.ocppVersion = this.ocppVersion;
            handshakeInfoImpl.identity = this.identity;
            return handshakeInfoImpl;
        }
    }
}
