package no.cantara.service.masterstatus;

import no.cantara.status.MasterStatusResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;

import static org.slf4j.LoggerFactory.getLogger;

@Path(BlueGreenMasterStatusResource.BLUEGREEN_STATUS_PATH)
public class BlueGreenMasterStatusResource extends MasterStatusResource {
    private static final Logger log = getLogger(BlueGreenMasterStatusResource.class);
    public static final String BLUEGREEN_STATUS_PATH = "bluegreenstatus";
    private final BlueGreenService blueGreenService;

    @Autowired
    public BlueGreenMasterStatusResource(BlueGreenService blueGreenService) {
        this.blueGreenService = blueGreenService;
    }

    @Override
    public boolean leavePrimary() {
        if (blueGreenService.mayTransformationBeStoppedNow()) {
            return super.leavePrimary();
        } else {
            return false;
        }
    }
}
