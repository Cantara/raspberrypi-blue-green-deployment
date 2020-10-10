package no.cantara.service.output;

import no.cantara.commands.config.ConstantValue;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OutputToApiSimulator implements OutputToApi {

    @Override
    public String doLogin(String username, String password) {
        return "{\"accessToken\": \"" + ConstantValue.ATOKEN + "\"," +
                "\"expires\": \"" + Instant.now().plusSeconds(600).toString() + "\"}";
    }

    @Override
    public boolean sendSampleMessages(String accessToken, List<String> messages) {
        boolean sentOk = false;
        if (accessToken != null && messages != null) {
            sentOk = true;
        }
        return sentOk;
    }

    @Override
    public Long sendMessages(String accessToken, List<String> messages) {
        return null;
    }
}
