package no.cantara.service.transform;

import no.cantara.service.input.InputQueueSimulator;
import no.cantara.service.output.OutputToApiSimulator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TransformService {
    private static final Logger log = getLogger(TransformService.class);

    private final InputQueueSimulator inputQueue;
    private final OutputToApiSimulator outputToApi;
    private boolean isReadingFromQueue;
    private boolean isWritingToApi;
    private boolean doTransform = false;
    private Thread importThread = null;

    @Autowired
    public TransformService(InputQueueSimulator inputQueue, OutputToApiSimulator outputToApi) {
        this.inputQueue = inputQueue;
        this.outputToApi = outputToApi;
    }

    public void startImport() {
        doTransform = true;
        if (importThread == null) {
            importThread = new Thread(() -> {
                while(doTransform) {
                    try {
                        importAndTransform();
                        Thread.sleep(100);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            importThread.start();
        }
    }

    public void stopImport() {
        doTransform = false;
        importThread = null;
    }

    public void pauseImport() {
        doTransform = false;
    }

    public void importAndTransform() {
        if (isReadingFromQueue) {
            log.trace("Reading from queue");
            if (isWritingToApi) {
                log.trace("Write to api");
            };
        }
    }


    public void readFromQueue(boolean shouldRead) {
        this.isReadingFromQueue = shouldRead;
        if (shouldRead) {
            importAndTransform();
        }
    }

    public boolean isReadingFromQueue() {
        return isReadingFromQueue;
    }

    public void writeToApi(boolean shouldWrite) {
        this.isWritingToApi = shouldWrite;
    }

    public boolean isWritingToApi() {
        return isWritingToApi;
    }
}
