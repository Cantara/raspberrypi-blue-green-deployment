package no.cantara.service.masterstatus;

import no.cantara.status.MasterStatusResource;

import javax.ws.rs.Path;

@Path("client/" + MasterStatusResource.DEFAULT_PATH)
public class BlueGreenMasterStatusResource extends MasterStatusResource {

}
