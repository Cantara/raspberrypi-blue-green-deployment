package no.cantara.util;

import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.Resource;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class Configuration {
    private static final Logger log = getLogger(Configuration.class);
    
    private static final ConstrettoConfiguration configuration = new ConstrettoBuilder()
            .createPropertiesStore()
            .addResource(Resource.create("classpath:application.properties"))
            .addResource(Resource.create("file:./config_override/application_override.properties"))
            .done()
            .getConfiguration();
    
    private Configuration() {}
    
    public static String getString(String key) {
        return configuration.evaluateToString(key);
    }
    public static String getString(String key, String defaultValue) {
        String value = defaultValue;
        try {
            value = configuration.evaluateToString(key);
        } catch (Exception e) { 
            log.trace("Failed to load configruation for: {}", key,e);
        }
        return value;
    }
    
    public static Integer getInt(String key) {
        return configuration.evaluateToInt(key);
    }

    public static Integer getInt(String key, int defaultValue) {
        return configuration.evaluateTo(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return configuration.evaluateToBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        boolean value = false;
        try {
            value = configuration.evaluateToBoolean(key);
        } catch (Exception e) {
            log.trace("Failed to load configruation for: {}", key,e);
        }
        return value;
    }
}