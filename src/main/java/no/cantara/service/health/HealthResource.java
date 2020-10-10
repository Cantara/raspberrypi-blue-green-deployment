package no.cantara.service.health;

import no.cantara.service.masterstatus.BlueGreenService;
import no.cantara.status.MasterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

/**
 * Simple health endpoint for checking the server is running
 *
 * @author <a href="mailto:asbjornwillersrud@gmail.com">Asbjørn Willersrud</a> 30/03/2016.
 */
@Path(HealthResource.HEALTH_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    public static final String HEALTH_PATH = "health";
    private static final Logger log = LoggerFactory.getLogger(HealthResource.class);

    private final BlueGreenService blueGreenService;

    @Autowired
    public HealthResource(BlueGreenService blueGreenService) {
        this.blueGreenService = blueGreenService;
        this.blueGreenService.startWarmup();
    }

    @GET
    public Response healthCheck() {
        log.trace("healthCheck");
        String response = String.format("{ \"microservice-health\": \"%s\", \"masterStatus\": \"%s\", \"version\": \"%s\", \"now\":\"%s\", \"running since\": \"%s\"}",
                getHealth(), getMasterStatus(), getVersion(), Instant.now(), getRunningSince());
        return Response.ok(response).build();
    }

    private String getHealth() {
        MasterStatus.Status status = MasterStatus.getStatus();
        if (status == MasterStatus.Status.FAILED) {
            return "unhealthy";
        } else {
            return "OK";
        }
    }


    private String getMasterStatus() {
        return MasterStatus.getStatus().name();
    }


    private String getRunningSince() {
        long uptimeInMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        return Instant.now().minus(uptimeInMillis, ChronoUnit.MILLIS).toString();
    }

    private String getVersion() {
        Properties mavenProperties = new Properties();
        String resourcePath = "/META-INF/maven/no.cantara.service/microservice-baseline/pom.properties";
        URL mavenVersionResource = this.getClass().getResource(resourcePath);
        if (mavenVersionResource != null) {
            try {
                mavenProperties.load(mavenVersionResource.openStream());
                return mavenProperties.getProperty("version", "missing version info in " + resourcePath);
            } catch (IOException e) {
                log.warn("Problem reading version resource from classpath: ", e);
            }
        }
        return "(DEV VERSION)";
    }
}