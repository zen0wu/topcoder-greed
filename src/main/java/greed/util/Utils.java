package greed.util;

import greed.conf.ConfigException;
import greed.conf.schema.GreedConfig;

/**
 * Greed is good! Cheers!
 */
public class Utils {
    private static boolean initialized = false;
    private static GreedConfig greedConfig = null;

    public static void initialize() throws ConfigException {
        if (!initialized) {
            initialized = true;
            greedConfig = Configuration.loadConfig();
            Log.initialize(greedConfig.getLogging());
        }
    }

    public static void reinitialize() throws ConfigException {
        initialized = false;
        initialize();
    }

    public static GreedConfig getGreedConfig() {
        if (greedConfig == null) {
            Log.e("Uninitialized config");
        }
        return greedConfig;
    }
}
