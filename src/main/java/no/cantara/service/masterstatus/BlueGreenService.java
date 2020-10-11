package no.cantara.service.masterstatus;

import no.cantara.service.input.InputQueue;
import no.cantara.service.output.OutputToApiSimulator;
import no.cantara.service.transform.TransformService;
import no.cantara.status.FeatureStatus;
import no.cantara.status.HealthValidator;
import no.cantara.status.MasterStatus;
import no.cantara.util.Configuration;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static no.cantara.service.transform.TransformService.*;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BlueGreenService extends HealthValidator {
    private static final Logger log = getLogger(BlueGreenService.class);

    private final InputQueue inputQueue;
    private final OutputToApiSimulator outputToApi;
    private final TransformService transformService;
    private boolean warmupStarted = false;

    @Autowired
    public BlueGreenService(InputQueue inputQueue, OutputToApiSimulator outputToApi, TransformService transformService) {
        this.inputQueue = inputQueue;
        this.outputToApi = outputToApi;
        this.transformService = transformService;
    }

    public boolean mayTransformationBeStoppedNow() {
        transformService.pauseImport();
        return true;
    }

    @Override
    public void startWarmup() {
        if (!warmupStarted) {
            warmupStarted = true;
            super.startWarmup();
            boolean inputIsOk = verifyIntegrationToInputQueue();
            boolean outputIsOk = verifyIntegrationToOutputApi();
            if (inputIsOk && outputIsOk) {
                if (startAsFallbackNode()) {
                    MasterStatus.setStatus(MasterStatus.Status.FALLBACK);
                } else if (startAsPrimary()) {
                    MasterStatus.setStatus(MasterStatus.Status.PRIMARY);
                    FeatureStatus.enable(WRITE_TO_API);
                    FeatureStatus.enable(READ_FROM_QUEUE);
                    FeatureStatus.enable(IMPORT_AND_TRANSFORM);
                    transformService.startImport();
                } else if (startAsActive()) {
                    MasterStatus.setStatus(MasterStatus.Status.ACTIVE);
                    FeatureStatus.enable(WRITE_TO_API);
                    FeatureStatus.enable(READ_FROM_QUEUE);
                    FeatureStatus.enable(IMPORT_AND_TRANSFORM);
                    transformService.startImport();
                } else {
                    MasterStatus.setStatus(MasterStatus.Status.FAILED);
                }
            }

            log.info("Warmup status. Integration status: \n" +
                    "  input: {}\n" +
                    "  output: {}", mapBoolean(inputIsOk), mapBoolean(outputIsOk));
        }
    }

    boolean startAsActive() {
        boolean startAsActive = false;
        String primaryUrl = Configuration.getString("primaryUrl", null);
        if (primaryUrl == null) {
            startAsActive = true;
        } else {
            log.warn("PrimaryURL is provided. This node may not be started in Active Status.");
        }
        return startAsActive;
    }

    boolean startAsPrimary() {
        boolean startAsPrimary = false;
        String primaryUrl = Configuration.getString("primaryUrl", null);
        if (primaryUrl != null && !primaryUrl.isEmpty()) {
            startAsPrimary = primaryExistAndWillDegradToFallback(primaryUrl);
        }
        return startAsPrimary;
    }

     boolean primaryExistAndWillDegradToFallback(String primaryUrl) {
        boolean canResumePrimary = false;
        try {
            URI primaryUri = URI.create(primaryUrl);
            canResumePrimary = PrimaryClient.requestPrimary(primaryUri);
        } catch (Exception e) {
            log.warn("Failed to resume primary. Reason: {}", e);
        }
        return canResumePrimary;
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

    boolean startAsFallbackNode() {
        boolean startInFallbackStatus =  false;
        if (Configuration.getString("masterStatus", "").contains("FALLBACK")) {
            startInFallbackStatus = true;
        }
        return startInFallbackStatus;
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
