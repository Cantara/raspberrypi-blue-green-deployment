package no.cantara.service.input;

import no.cantara.commands.config.ConstantValue;
import no.cantara.status.IntegrationStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class InputQueueSimulator implements InputQueue {
    private boolean integrationOk = false;
    private long messageCount = 0;

    @Override
    public boolean isIntegrationOk() {
        return integrationOk;
    }

    @Override
    public IntegrationStatus getIntegrationStatus() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("messageCount", messageCount);
        return new IntegrationStatus("InputQueueSimulator",integrationOk, metadata);
    }

    @Override
    public String doLogin(String username, String password) {
        return "{\"accessToken\": \"" + ConstantValue.ATOKEN + "\"," +
                "\"expires\": \"" + Instant.now().plusSeconds(600).toString() + "\"}";
    }

    @Override
    public List<String> fetchSampleMessages(String accessToken, int count) {
        //accessToken included solely for example usage
        List<String> messages = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                messages.add("A message to you " + i);
            }
        }
        return messages;
    }

    @Override
    public List<String> fetchMessages(String accessToken) {
        List<String> messages = new ArrayList<>();
        Random r = new Random();
        int newMessageCount = r.nextInt((5 - 0) + 1) + 0;
        for (int i = 0; i < newMessageCount; i++) {
            messageCount ++;
            messages.add("A message to you " + messageCount);
        }
        return messages;
    }
}
