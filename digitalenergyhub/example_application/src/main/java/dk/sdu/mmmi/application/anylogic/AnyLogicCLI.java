package dk.sdu.mmmi.application.anylogic;

import java.io.IOException;

/**
 * This class is an abstraction over the AnyLogic CLI (command-line interface).
 */
public class AnyLogicCLI {
    private final String anylogicExecutablePath;

    /**
     * Constructs a new AnyLogicCLI
     * @param anylogicExecutablePath E.g. C:/Program Files/AnyLogic 8.8 Personal Learning Edition/anylogic.exe
     */
    public AnyLogicCLI(String anylogicExecutablePath) {
        this.anylogicExecutablePath = anylogicExecutablePath;
    }

    /**
     * Open the given model and run the experiment.
     *
     * See: https://anylogic.help/anylogic/running/export-java-application.html#run-experiment-cmd
     * @param modelPath The absolute path e.g. C:/Users/csbc/MyAnyLogicModel/MyAnyLogicModel.alp
     * @param experimentName The name of the experiment e.g. MyExperiment
     */
    public void run(String modelPath, String experimentName) {
        String cmd = String.format("%s -run \"%s\" \"%s\"",
                anylogicExecutablePath,
                modelPath,
                experimentName);

        Runtime runtime = Runtime.getRuntime();

        try {
            Process anyLogicProcess = runtime.exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("\nProvide arguments: \n" +
                    "(1) anylogic executable path e.g. \"C:/Program Files/AnyLogic 8.8 Personal Learning " +
                    "Edition/anylogic.exe\" \n" +
                    "(2) anylogic model path e.g. \"C:/Users/csbc/MyAnyLogicModel/MyAnyLogicModel.alp\" \n" +
                    "(3) the anylogic experiment name e.g. \"Simulation\" \n" +
                    "\nExample usage: \n" +
                    "AnyLogicStarter \"C:/Program Files/AnyLogic 8.8 Personal Learning Edition/anylogic.exe\" " +
                    "\"C:/RealTimeModel.alp\" \"Simulation\"");
        }

        String anylogicPath = args[0];
        String anylogicModel = args[1];
        String anylogicExperiment = args[2];

        new AnyLogicCLI(anylogicPath)
                .run(anylogicModel, anylogicExperiment);
    }
}
