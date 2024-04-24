module dk.sdu.mmmi.anylogic_remote_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires io.nats.jnats;

    opens dk.sdu.mmmi.anylogic_remote_ui to javafx.fxml;
    exports dk.sdu.mmmi.anylogic_remote_ui;
}