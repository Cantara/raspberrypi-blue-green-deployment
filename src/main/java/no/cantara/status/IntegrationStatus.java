package no.cantara.status;

import java.util.HashMap;
import java.util.Map;

public class IntegrationStatus {
    boolean integrationOk;
    final Map<String,Object> metadata;

    public IntegrationStatus(boolean integrationOk) {
        this.integrationOk = integrationOk;
        metadata = new HashMap();
    }

    public IntegrationStatus(boolean integrationOk, Map<String, Object> metadata) {
        this.integrationOk = integrationOk;
        if (metadata != null) {
            this.metadata = metadata;
        } else {
            this.metadata = new HashMap<>();
        }
    }

    public boolean isIntegrationOk() {
        return integrationOk;
    }

    public void setIntegrationOk(boolean integrationOk) {
        this.integrationOk = integrationOk;
    }

    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
