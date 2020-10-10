package no.cantara.service.output;

import no.cantara.status.IntegrationStatusProvider;

import java.util.List;

public interface OutputToApi extends IntegrationStatusProvider {

    String doLogin(String username, String password);
    boolean sendSampleMessages(String accessToken, List<String> messages);
    Long sendMessages(String accessToken, List<String> messages);
}
