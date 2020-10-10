package no.cantara.service.input;

import no.cantara.status.IntegrationStatusProvider;

import java.util.List;

public interface InputQueue extends IntegrationStatusProvider {

    String doLogin(String username, String password);
    List<String> fetchSampleMessages(String accessToken, int count);
}
