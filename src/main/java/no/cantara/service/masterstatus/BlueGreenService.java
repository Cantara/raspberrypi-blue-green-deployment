package no.cantara.service.masterstatus;

import no.cantara.service.transform.TransformService;
import no.cantara.status.HealthValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BlueGreenService extends HealthValidator {
    private static final Logger log = getLogger(BlueGreenService.class);

    private final TransformService transformService;

    @Autowired
    public BlueGreenService(TransformService transformService) {
        this.transformService = transformService;
    }

    public boolean canStopTransform() {
        return false;
    }

    @Override
    public void startWarmup() {
        super.startWarmup();
    }
}
