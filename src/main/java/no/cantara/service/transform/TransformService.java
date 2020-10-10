package no.cantara.service.transform;

import no.cantara.service.input.InputQueueSimulator;
import no.cantara.service.output.OutputToApiSimulator;
import no.cantara.status.FeatureStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TransformService {
    private static final Logger log = getLogger(TransformService.class);
    public static final String READ_FROM_QUEUE = "readFromQueue";
    public static final String WRITE_TO_API = "writeToApi";
    public static final String IMPORT_AND_TRANSFORM = "importAndTransform";

    private final InputQueueSimulator inputQueue;
    private final OutputToApiSimulator outputToApi;
    private boolean doTransform = false;
    private Thread importThread = null;

    @Autowired
    public TransformService(InputQueueSimulator inputQueue, OutputToApiSimulator outputToApi) {
        this.inputQueue = inputQueue;
        this.outputToApi = outputToApi;
    }

    public void startImport() {
        doTransform = true;
        FeatureStatus.enable(IMPORT_AND_TRANSFORM);
        if (importThread == null || importThread.getState() == Thread.State.TERMINATED) {
            importThread = new Thread(() -> {
                while(doTransform) {
                    try {
                        importAndTransform();
                        Thread.sleep(2000);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            importThread.setName("ImportThread");
            importThread.start();
        }
    }

    public void stopImport() {
        doTransform = false;
        FeatureStatus.disable(IMPORT_AND_TRANSFORM);
        importThread = null;
    }

    public void pauseImport() {
        doTransform = false;
        FeatureStatus.disable(IMPORT_AND_TRANSFORM);
    }

    public void importAndTransform() {
        boolean readFromQueue = shouldReadFromQueue();
        if (readFromQueue) {
            log.trace("Reading from queue");
            boolean writeToApi = shouldWriteToApi();
            if (writeToApi) {
                log.trace("Write to api");
            };
        }
    }

    public boolean shouldReadFromQueue() {
        Boolean enabled = FeatureStatus.isEnabled(READ_FROM_QUEUE);
        return enabled;
    }

    public boolean shouldWriteToApi() {
        Boolean enabled = FeatureStatus.isEnabled(WRITE_TO_API);
        return enabled;
    }
}
