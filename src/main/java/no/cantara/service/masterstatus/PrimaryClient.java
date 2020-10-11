package no.cantara.service.masterstatus;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.slf4j.LoggerFactory.getLogger;

public class PrimaryClient {
    private static final Logger log = getLogger(PrimaryClient.class);

    public static boolean requestPrimary(URI primaryUri) {
        boolean acceptPrimary = false;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .uri(primaryUri)
                .build();
        HttpResponse<String> response =
                null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null) {
                switch (response.statusCode()) {
                    case 202:
                        acceptPrimary = true;
                        break;
                    case 412:
                        String body = response.body();
                        log.info("Node on url: {} may not give away primary control. Reason {}", primaryUri, body);
                        break;
                    case 102:
                        log.info("We need to wait...");
                        //TODO
                        break;
                    default:
                        log.info("Unexpected http response: {}", response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return acceptPrimary;
    }
}
