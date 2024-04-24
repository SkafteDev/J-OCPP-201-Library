package dk.sdu.mmmi.anylogic_remote_ui;

import dk.sdu.mmmi.anylogic.AnyLogicCLI;
import dk.sdu.mmmi.anylogic.NatsUtils;
import dk.sdu.mmmi.anylogic.messaging.AnyLogicMessageType;
import dk.sdu.mmmi.anylogic.messaging.IMessageRouteResolver;
import dk.sdu.mmmi.anylogic.messaging.MessageRouteResolverImpl;
import io.nats.client.*;
import io.nats.client.impl.NatsMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerUIController {

    public TitledPane grpServerSettings;
    public TitledPane grpAnyLogicSettings;
    public TextField txtAnyLogicExePath;
    public TextField txtAnyLogicModelFile;
    public TextField txtAnyLogicExperiment;
    public TitledPane grpOutput;
    public TextArea txtOutput;
    public Button btnStartServer;
    public TextField txtNatsUrl;
    public TextField txtServerInstanceId;
    private AnyLogicCLI anyLogicCLI;
    private boolean startBtnIsToggled = false;
    private Subscription startAnyLogicSubscription;
    private Dispatcher natsDispatcher;
    private IMessageRouteResolver routeResolver;
    private Connection natsConnection;

    @FXML
    protected void onStartAnyLogic() {
        if (startBtnIsToggled) {
            stopServer();
            lockUI(false);
            btnStartServer.setText("Start server");
            startBtnIsToggled = !startBtnIsToggled;
        } else {
            if (startServer()) {
                lockUI(true);
                btnStartServer.setText("Stop server");
                startBtnIsToggled = !startBtnIsToggled;
            }
        }
    }

    private void stopServer() {
        natsDispatcher.unsubscribe(startAnyLogicSubscription);
        anyLogicCLI.close();
        anyLogicCLI = null;
        try {
            natsConnection.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        write("Stopped server.");
    }

    private boolean startServer() {
        String natsUrl = txtNatsUrl.getText();
        String serverInstanceId = txtServerInstanceId.getText();
        String anyLogicExecutable = txtAnyLogicExePath.getText();
        String anyLogicModel = txtAnyLogicModelFile.getText();
        String anylogicExperiment = txtAnyLogicExperiment.getText();

        if (!checkExists(anyLogicExecutable)) {
            write("The given AnyLogic executable does not exist " + anyLogicExecutable);
            return false;
        }

        if (!checkExists(anyLogicModel)) {
            write("The given AnyLogic model does not exist " + anyLogicExecutable);
            return false;
        }

        write("Establishing connection to NATS.io.");
        this.natsConnection = NatsUtils.createConnection(Options.builder()
                .server(natsUrl)
                .build()
        );

        /*
         * Prepare the anylogic cli.
         */
        if (anyLogicCLI == null) {
            this.anyLogicCLI = new AnyLogicCLI(anyLogicExecutable);
        }

        /*
         * Add the server request handlers.
         */
        this.routeResolver = getMessageRouteResolver(serverInstanceId);
        String subject = routeResolver.getRoute(AnyLogicMessageType.StartAnyLogicProcessRequest);
        this.natsDispatcher = natsConnection.createDispatcher();

        this.startAnyLogicSubscription = natsDispatcher.subscribe(subject, message -> {
            write("Received " + AnyLogicMessageType.StartAnyLogicProcessRequest);
            String payload = null;
            if (anyLogicCLI.run(anyLogicModel, anylogicExperiment)) {
                payload = "OK";
            } else {
                write("Could not start the AnyLogic process given the path " + anyLogicExecutable);
                payload = "ERROR";
            }

            write("Sending response " + payload);

            Message response = NatsMessage.builder()
                    .subject(message.getReplyTo())
                    .data(payload.getBytes(StandardCharsets.UTF_8))
                    .build();

            natsConnection.publish(response);
        });

        write(String.format("Waiting for incoming message on: %s",
                getBrokerAndSubjectAddr(AnyLogicMessageType.StartAnyLogicProcessRequest)));

        return true;
    }

    private boolean checkExists(String anyLogicExecutable) {
        return new File(anyLogicExecutable).exists();
    }

    private void lockUI(boolean b) {
        grpAnyLogicSettings.setDisable(b);
        grpServerSettings.setDisable(b);
    }

    private static IMessageRouteResolver getMessageRouteResolver(String serverInstanceId) {
        return new MessageRouteResolverImpl(serverInstanceId);
    }

    private void write(String txt) {
        // Update the UI element on the UI thread.
        Platform.runLater(() -> {
            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
            String formattedTimeStamp = now.format(formatter);

            String out = String.format("%s: %s", formattedTimeStamp, txt);

            if (!txtOutput.getText().isEmpty()) {
                txtOutput.appendText("\n\n");
            }

            txtOutput.appendText(out);
        });
    }

    private String getBrokerAndSubjectAddr(AnyLogicMessageType msgType) {
        IMessageRouteResolver messageRouteResolver = getMessageRouteResolver(txtServerInstanceId.getText());
        return txtNatsUrl.getText() + "@" + messageRouteResolver.getRoute(msgType);
    }
}