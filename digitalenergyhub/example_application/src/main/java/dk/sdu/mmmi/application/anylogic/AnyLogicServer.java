package dk.sdu.mmmi.application.anylogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Message;
import io.nats.client.Options;
import io.nats.client.Subscription;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class AnyLogicServer {
    public static void main(String[] args) {
        io.nats.client.Connection nc = NatsUtils.createConnection(Options.builder()
                .server("nats://localhost:4222")
                .build()
        );

        try {
            Subscription sub = nc.subscribe("anylogic.commands.start");
            Message msg = sub.nextMessage(Duration.ZERO);
            String jsonPayload = new String(msg.getData(), StandardCharsets.UTF_8);

            System.out.println(jsonPayload);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);

            String startDateStr = jsonNode.get("startDate").asText();
            String stopDateStr = jsonNode.get("stopDate").asText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = dateFormat.parse(startDateStr);
            Date stopDate = dateFormat.parse(stopDateStr);

            System.out.println(startDate);
            System.out.println(stopDate);

            nc.close();
        } catch (JsonProcessingException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
