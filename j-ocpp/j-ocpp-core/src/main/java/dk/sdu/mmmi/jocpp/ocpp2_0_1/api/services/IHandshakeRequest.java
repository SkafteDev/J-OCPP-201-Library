package dk.sdu.mmmi.jocpp.ocpp2_0_1.api.services;

public interface IHandshakeRequest {

    /**
     * The identity of the CS.
     */
    String getCsIdentity();


    OcppVersion getOcppVersion();
}
