package dk.sdu.mmmi.anylogic.messaging;

public enum AnyLogicMessageType {
    StartAnyLogicProcessRequest("StartAnyLogicProcess"),
    StartAnyLogicProcessResponse("StartAnyLogicProcess"),
    RunExperimentRequest("RunExperiment"),
    RunExperimentResponse("RunExperiment");

    private final String value;

    AnyLogicMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
