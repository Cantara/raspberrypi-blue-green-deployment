package no.cantara.status;

import java.util.HashMap;

public class FeatureStatus {

    private static FeatureStatus instance = null;

    // variable of type String
    private HashMap<String, Boolean> featureStatus;

    // private constructor restricted to this class itself
    private FeatureStatus() {
        featureStatus = new HashMap<>();
    }

    public static Boolean isEnabled(String feature) {
        return getInstance().featureStatus.get(feature);
    }

    public static void enable(String feature) {
        updateStatus(feature, false);
    }

    public static void disable(String feature) {
        updateStatus(feature, false);
    }

    public static void remove(String feature) {
        updateStatus(feature, null);
    }

    protected static void updateStatus(String feature, Boolean status) {
        if (feature != null && !feature.isEmpty()) {
            if (status != null) {
                getInstance().featureStatus.put(feature, status);
            } else {
                getInstance().featureStatus.remove(feature);
            }
        }
    }


    public static FeatureStatus getInstance() {
        return instance;
    }
}

