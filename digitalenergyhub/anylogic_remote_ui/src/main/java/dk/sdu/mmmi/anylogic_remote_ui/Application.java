package dk.sdu.mmmi.anylogic_remote_ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("AnyLogic App");
        stage.setScene(scene);
        stage.show();

        ApplicationController controller = fxmlLoader.getController();
        controller.setParentStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}