package no.cantara.service.masterstatus;

import no.cantara.service.input.InputQueue;
import no.cantara.service.output.OutputToApiSimulator;
import no.cantara.service.transform.TransformService;
import no.cantara.status.HealthValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BlueGreenService extends HealthValidator {
    private static final Logger log = getLogger(BlueGreenService.class);

    private final InputQueue inputQueue;
    private final OutputToApiSimulator outputToApi;
    private final TransformService transformService;

    @Autowired
    public BlueGreenService(InputQueue inputQueue, OutputToApiSimulator outputToApi, TransformService transformService) {
        this.inputQueue = inputQueue;
        this.outputToApi = outputToApi;
        this.transformService = transformService;
    }

    public boolean mayTransformationBeStoppedNow() {
        return false;
    }

    @Override
    public void startWarmup() {
        super.startWarmup();
        boolean inputIsOk = verifyIntegrationToInputQueue();
        boolean outputIsOk = verifyIntegrationToOutuptApi();

        log.info("Warmup status. Integration status: \n" +
                "  input: {}\n" +
                "  output: {}", mapBoolean(inputIsOk), mapBoolean(outputIsOk));
    }

    boolean verifyIntegrationToInputQueue() {
        boolean verified = false;
        String token = inputQueue.doLogin("user", "password");
        if (token != null && token.contains("accessToken")) {
            List<String> messages = inputQueue.fetchSampleMessages("anything", 5);
            if (messages != null && messages.size() == 5) {
                verified = true;
            }
        }
        return verified;
    }

    boolean verifyIntegrationToOutuptApi() {
        return false;
    }

    private String mapBoolean(boolean value) {
        if (value) {
            return "OK";
        }
        return "FAILED";
    }
}
