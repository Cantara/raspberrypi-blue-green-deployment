package no.cantara.status;

public abstract class HealthValidator {

    public void startWarmup() {
        MasterStatus.setStatus(MasterStatus.Status.WARMUP);
    }
}
