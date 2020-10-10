package no.cantara.service.input;

import no.cantara.commands.config.ConstantValue;
import no.cantara.status.IntegrationStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class InputQueueSimulator implements InputQueue {
    private boolean integrationOk = false;

    @Override
    public boolean isIntegrationOk() {
        return integrationOk;
    }

    @Override
    public IntegrationStatus getIntegrationStatus() {
        return new IntegrationStatus(integrationOk);
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
}
