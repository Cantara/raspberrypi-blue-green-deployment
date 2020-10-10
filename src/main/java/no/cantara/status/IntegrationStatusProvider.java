package no.cantara.status;

public interface IntegrationStatusProvider {

    boolean isIntegrationOk();
    IntegrationStatus getIntegrationStatus();
}
