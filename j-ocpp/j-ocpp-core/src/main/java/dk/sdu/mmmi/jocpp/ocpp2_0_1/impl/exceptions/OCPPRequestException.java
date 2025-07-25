
package dk.sdu.mmmi.jocpp.ocpp2_0_1.impl.exceptions;

public class OCPPRequestException extends RuntimeException {
    public OCPPRequestException(String msg) {super(msg); }
    public OCPPRequestException(String msg, Throwable cause) {super(msg, cause); }
}
