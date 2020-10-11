package no.cantara.service.health;

import no.cantara.service.testsupport.TestServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * @author <a href="mailto:asbjornwillersrud@gmail.com">Asbjørn Willersrud</a> 30/03/2016.
 */
public class HealthResourceTest {

    private TestServer testServer;

    @BeforeClass
    public void startServer() throws Exception {
        testServer = new TestServer(getClass());
        testServer.start();
    }

    @AfterClass
    public void stop() {
        testServer.stop();
    }

    @Test
    public void testHealth() throws IOException {
        given()
                .log().everything()
                .expect()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body(containsString("\"masterStatus\": \"ACTIVE\""))
                .log().everything()
                .when()
                .get(HealthResource.HEALTH_PATH);
    }

}