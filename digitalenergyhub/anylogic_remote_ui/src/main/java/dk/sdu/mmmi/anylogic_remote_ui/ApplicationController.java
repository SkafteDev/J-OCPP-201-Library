package dk.sdu.mmmi.anylogic_remote_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationController {
    private Stage parentStage;

    @FXML
    protected void onServer() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("anylogic-server.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.parentStage.setTitle("AnyLogic Server");
        this.parentStage.setScene(scene);
    }

    @FXML
    protected void onRemote() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("anylogic-remote.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.parentStage.setTitle("AnyLogic Remote");
        this.parentStage.setScene(scene);
    }

    public void setParentStage(Stage stage) {
        this.parentStage = stage;
    }
}
