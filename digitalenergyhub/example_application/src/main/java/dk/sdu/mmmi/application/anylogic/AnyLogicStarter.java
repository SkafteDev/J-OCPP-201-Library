package dk.sdu.mmmi.application.anylogic;

public class AnyLogicStarter {
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
