package no.cantara.service.output;

import no.cantara.commands.config.ConstantValue;
import no.cantara.status.IntegrationStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OutputToApiSimulator implements OutputToApi {

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
        return null;
    }
}
