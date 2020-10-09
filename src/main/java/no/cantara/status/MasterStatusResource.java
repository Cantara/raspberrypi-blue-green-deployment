package no.cantara.status;

import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

@Path(MasterStatusResource.DEFAULT_PATH)
public class MasterStatusResource {
    private static final Logger log = getLogger(MasterStatusResource.class);

    public static final String DEFAULT_PATH = "masterstatus";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasterStatus() {
        String body = String.format("{ \"masterstatus\":\"%s\"}", MasterStatus.getStatus());
        Response response = Response.ok(body).build();
        log.trace("getMasterStatusResponse: {}", response);
        return response;
    }

    @PUT
    @Path("requestPrimary")
    public Response requestPrimary() {
        Response response = null;
        MasterStatus.Status status = MasterStatus.getStatus();
        switch (status) {
            case PRIMARY:
                if (readyToLeavePrimary()) {
                    response = Response.status(202).build();
                } else {
                    response = Response.status(412, "This node is not Primary. Current MasterStatus is: " + status.name()).build();
                }
                break;
            default:
                response = Response.status(412, "This node is not Primary. Current MasterStatus is: " + status.name()).build();
        }
        log.info("Request Primary response: {}", response);
        return response;
    }

    public boolean readyToLeavePrimary() {
        return false;
    }

}
