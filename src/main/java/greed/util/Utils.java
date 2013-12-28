package greed.util;

import greed.conf.ConfigException;
import greed.conf.schema.GreedConfig;

/**
 * Greed is good! Cheers!
 */
public class Utils {
    private static GreedConfig greedConfig = null;

    public static void initialize() throws ConfigException {
        greedConfig = Configuration.loadConfig();
        Log.initialize(greedConfig.getLogging());
    }

    public static GreedConfig getGreedConfig() {
        if (greedConfig == null) {
            Log.e("Uninitialized config");
        }
        return greedConfig;
    }
}
