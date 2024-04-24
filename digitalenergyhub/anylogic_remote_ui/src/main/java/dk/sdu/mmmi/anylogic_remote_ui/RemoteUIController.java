package dk.sdu.mmmi.anylogic_remote_ui;

import dk.sdu.mmmi.anylogic.NatsUtils;
import dk.sdu.mmmi.anylogic.messaging.AnyLogicMessageType;
import dk.sdu.mmmi.anylogic.messaging.IMessageRouteResolver;
import dk.sdu.mmmi.anylogic.messaging.MessageRouteResolverImpl;
import io.nats.client.*;
import io.nats.client.impl.NatsMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class RemoteUIController {

    @FXML
    public TextField txtNatsUrl;
    @FXML
    public TextField txtServerInstanceId;
    public TextArea txtOutput;
    public DatePicker dtExperimentStartDate;
    public DatePicker dtExperimentStopDate;
    private Connection natsConnection;
    private IMessageRouteResolver routeResolver;

    @FXML
    protected void onStartAnyLogic() {
        String natsUrl = txtNatsUrl.getText();
        String serverInstanceId = txtServerInstanceId.getText();
        this.routeResolver = getMessageRouteResolver(serverInstanceId);

        write("Establishing connection to NATS.io.");
        this.natsConnection = NatsUtils.createConnection(Options.builder()
                .server(natsUrl)
                .build()
        );

        String subject = routeResolver.getRoute(AnyLogicMessageType.StartAnyLogicProcessRequest);

        write("Sending " + AnyLogicMessageType.StartAnyLogicProcessRequest + " to " + getBrokerAndSubjectAddr(AnyLogicMessageType.StartAnyLogicProcessRequest));
        CompletableFuture<Message> request = this.natsConnection.request(NatsMessage.builder()
                .subject(subject)
                .data("{}", StandardCharsets.UTF_8)
                .build());

        try {
            Message message = request.get();
            String payload = new String(message.getData(), StandardCharsets.UTF_8);
            write("Received " + payload);
        } catch (InterruptedException | ExecutionException e) {
            write(e.getMessage());
        }
    }

    @FXML
    protected void onRunExperiment() {
        LocalDate startDate = dtExperimentStartDate.getValue();
        LocalDate stopDate = dtExperimentStopDate.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String strStartDate = startDate.format(formatter);
        String strStopDate = stopDate.format(formatter);

        try {
            String jsonPayload = format("{" +
                            "\"startDate\": \"%s\"," +
                            "\"stopDate\": \"%s\"" +
                            "}",

                    // JSON parameters
                    strStartDate, strStopDate
            );

            String subject = routeResolver.getRoute(AnyLogicMessageType.RunExperimentRequest);

            natsConnection.publish("anylogic.commands.start", jsonPayload.getBytes(StandardCharsets.UTF_8));

            // Make sure the message goes through before we close
            natsConnection.flush(Duration.ZERO);
            natsConnection.close();
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }

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

    private static IMessageRouteResolver getMessageRouteResolver(String serverInstanceId) {
        return new MessageRouteResolverImpl(serverInstanceId);
    }
}