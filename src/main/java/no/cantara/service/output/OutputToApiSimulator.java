package no.cantara.service.output;

import no.cantara.commands.config.ConstantValue;
import no.cantara.status.IntegrationStatus;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class OutputToApiSimulator implements OutputToApi {
    private static final Logger log = getLogger(OutputToApiSimulator.class);

    private boolean integrationOk = false;
    private long messageCount = 0;
    private String lastMessageReceived = null;

    @Override
    public boolean isIntegrationOk() {
        return integrationOk;
    }

    @Override
    public IntegrationStatus getIntegrationStatus() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("messageCount", messageCount);
        return new IntegrationStatus("OutputToApiSimulator", integrationOk, metadata);
    }

    @Override
    public String doLogin(String username, String password) {
        integrationOk = true;
        return "{\"accessToken\": \"" + ConstantValue.ATOKEN + "\"," +
                "\"expires\": \"" + Instant.now().plusSeconds(600).toString() + "\"}";
    }

    @Override
    public boolean sendSampleMessages(String accessToken, List<String> messages) {
        boolean sentOk = false;
        if (accessToken != null && messages != null) {
            sentOk = true;
            integrationOk = true;
        }
        return sentOk;
    }

    @Override
    public Long sendMessages(String accessToken, List<String> messages) {
        Long messagesSent = null;
        if (accessToken != null && messages != null && messages.size() > 0) {
            log.trace("Sent messages: {}", messages);
            for (String message : messages) {
                this.lastMessageReceived = message;
                this.messageCount++;
            }
            messagesSent = Long.valueOf(messages.size());
        }
        return messagesSent;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public String getLastMessageReceived() {
        return lastMessageReceived;
    }
}
