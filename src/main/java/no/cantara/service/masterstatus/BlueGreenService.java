package no.cantara.service.masterstatus;

import no.cantara.service.input.InputQueue;
import no.cantara.service.output.OutputToApiSimulator;
import no.cantara.service.transform.TransformService;
import no.cantara.status.HealthValidator;
import no.cantara.status.MasterStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        boolean outputIsOk = verifyIntegrationToOutputApi();
        if (inputIsOk && outputIsOk) {
            MasterStatus.setStatus(MasterStatus.Status.ACTIVE);
            transformService.writeToApi(true);
            transformService.readFromQueue(true);
        }

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

    boolean verifyIntegrationToOutputApi() {
        boolean verified = false;
        String token = outputToApi.doLogin("user", "password");
        if (token != null && token.contains("accessToken")) {
            List<String> sampleMessages = buildSampleOutputMessages();
            boolean samplesSentOk = outputToApi.sendSampleMessages("anything", sampleMessages);
            if (samplesSentOk) {
                verified = true;
            }
        }
        return verified;
    }

    private List<String> buildSampleOutputMessages() {
        List<String> messages = new ArrayList<>();
        messages.add("Sending a message to you 1");
        messages.add("Sending a message to you 2");
        return messages;
    }

    private String mapBoolean(boolean value) {
        if (value) {
            return "OK";
        }
        return "FAILED";
    }
}
