package dk.sdu.mmmi.digitalenergyhub.ocpp2_0_1.messagetypes;

public class OCPPMessageToSubjectMapping {

    public enum OCPPMessageType {

        SetChargingProfileRequest("setchargingprofilerequest"),
        SetChargingProfileResponse("setchargingprofileresponse"),
        StatusNotificationRequest("statusnotificationrequest"),
        StatusNotificationResponse("statusnotificationresponse");

        private final String subject;

        private OCPPMessageType(String subject) {
            this.subject = subject;
        }

        @Override
        public String toString() {
            return subject;
        }
    }
}
