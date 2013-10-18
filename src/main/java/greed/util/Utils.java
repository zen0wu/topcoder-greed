package greed.util;

import greed.conf.ConfigException;
import greed.conf.schema.GreedConfig;

/**
 * Greed is good! Cheers!
 */
public class Utils {
    private static boolean initialized = false;

    public static void initialize() throws ConfigException {
        if (!initialized) {
            initialized = true;
            GreedConfig config = Configuration.loadConfig();
            Log.initialize(config.getLogging());
        }
    }

    public static void reinitialize() throws ConfigException {
        initialized = false;
        initialize();
    }
}
