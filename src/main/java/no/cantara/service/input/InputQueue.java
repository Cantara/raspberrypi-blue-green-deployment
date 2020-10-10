package no.cantara.service.input;

import java.util.List;

public interface InputQueue {

    String doLogin(String username, String password);
    List<String> fetchSampleMessages(String accessToken, int count);
}
