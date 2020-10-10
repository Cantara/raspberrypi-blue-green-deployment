package no.cantara.service.output;

import java.util.List;

public interface OutputToApi {

    String doLogin(String username, String password);
    boolean sendSampleMessages(String accessToken, List<String> messages);
    Long sendMessages(String accessToken, List<String> messages);
}
