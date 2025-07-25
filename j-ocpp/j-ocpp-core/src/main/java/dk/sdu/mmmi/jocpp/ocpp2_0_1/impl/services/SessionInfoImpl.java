
package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.services;

import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.IOCPPSession;
import dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services.OcppVersion;

public class SessionInfoImpl implements IOCPPSession.SessionInfo {
    private String csId = null;
    private String csmsId = null;
    private String connectionURI = null;
    private String transportType = null;
    private OcppVersion ocppVersion;

    @Override
    public String getCsId() {
        return csId;
    }

    @Override
    public String getCsmsId() {
        return csmsId;
    }

    @Override
    public String getConnectionURI() {
        return connectionURI;
    }

    @Override
    public String getTransportType() {
        return transportType;
    }

    @Override
    public OcppVersion getOCPPVersion() {
        return ocppVersion;
    }

    @Override
    public String toString() {
        return "SessionInfoImpl{" +
                "csId='" + csId + '\'' +
                ", csmsId='" + csmsId + '\'' +
                ", connectionURI='" + connectionURI + '\'' +
                ", transportType='" + transportType + '\'' +
                ", ocppVersion=" + ocppVersion +
                '}';
    }

    public static final class SessionInfoImplBuilder {
        private String csId;
        private String csmsId;
        private String connectionURI;
        private String transportType;
        private OcppVersion ocppVersion;

        private SessionInfoImplBuilder() {
        }

        public static SessionInfoImplBuilder newBuilder() {
            return new SessionInfoImplBuilder();
        }

        public SessionInfoImplBuilder withCsId(String csId) {
            this.csId = csId;
            return this;
        }

        public SessionInfoImplBuilder withCsmsId(String csmsId) {
            this.csmsId = csmsId;
            return this;
        }

        public SessionInfoImplBuilder withConnectionURI(String connectionURI) {
            this.connectionURI = connectionURI;
            return this;
        }

        public SessionInfoImplBuilder withTransportType(String transportType) {
            this.transportType = transportType;
            return this;
        }

        public SessionInfoImplBuilder withOcppVersion(OcppVersion ocppVersion) {
            this.ocppVersion = ocppVersion;
            return this;
        }

        public SessionInfoImpl build() {
            SessionInfoImpl sessionInfoImpl = new SessionInfoImpl();
            sessionInfoImpl.transportType = this.transportType;
            sessionInfoImpl.csmsId = this.csmsId;
            sessionInfoImpl.csId = this.csId;
            sessionInfoImpl.connectionURI = this.connectionURI;
            sessionInfoImpl.ocppVersion = this.ocppVersion;
            return sessionInfoImpl;
        }
    }
}
