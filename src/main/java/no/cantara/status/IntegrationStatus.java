package no.cantara.status;

import java.util.HashMap;
import java.util.Map;

public class IntegrationStatus {
    private final String name;
    private boolean integrationOk;
    private final Map<String,Object> metadata;

    public IntegrationStatus(String name, boolean integrationOk) {
        this(name, integrationOk,new HashMap<>());
    }

    public IntegrationStatus(String name, boolean integrationOk, Map<String, Object> metadata) {
        this.name = name;
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

    @Override
    public String toString() {
        return "IntegrationStatus{" +
                "name='" + name + '\'' +
                ", integrationOk=" + integrationOk +
                ", metadata=" + metadata +
                '}';
    }
}
