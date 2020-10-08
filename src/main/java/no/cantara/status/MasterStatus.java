package no.cantara.status;

public class MasterStatus {
   public enum Status { INITIAL, WARMUP, PRIMARY, SECONDARY, FALLBACK, DRAINING, FAILED, ACTIVE };

   private Status status;

   private static MasterStatus instance = null;
    public MasterStatus() {
        this.status = Status.INITIAL;
    }
    public synchronized void setStatus(Status status) {
        getInstance().status = status;
    }
    public synchronized static Status getStatus() {
        return getInstance().status;
    }

    protected static MasterStatus getInstance() {
        if (instance == null) {
            instance = new MasterStatus();
        }
        return instance ;
    }
}
