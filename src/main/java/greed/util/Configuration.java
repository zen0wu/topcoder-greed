package greed.util;

import com.topcoder.client.contestApplet.common.LocalPreferences;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import greed.conf.ConfigException;
import greed.conf.ConfigParser;
import greed.conf.schema.GreedConfig;

import java.io.File;
import java.io.IOException;

/**
 * Greed is good! Cheers!
 */
@SuppressWarnings("unused")
public class Configuration {
    // Workspace related
    private static LocalPreferences pref = LocalPreferences.getInstance();

    private static final String GREED_WORKSPACE_KEY = "greed.workspace";
    public static final String TEMPLATE_PATH = "/templates";

    public static boolean workspaceSet() {
        String workspace = getWorkspace();
        return workspace != null && workspace.length() > 0;
    }

    public static String getWorkspace() {
        return pref.getProperty(GREED_WORKSPACE_KEY);
    }

    public static void setWorkspace(String workspace) {
        pref.setProperty(GREED_WORKSPACE_KEY, workspace);
        try {
            pref.savePreferences();
        } catch (IOException e) {
            Log.e("Save working space error", e);
        }
    }

    private static final String DEFAULT_USER_CONFIG_FILENAME = "greed.conf";

    static GreedConfig loadConfig() throws ConfigException {
        Config conf = ConfigFactory.parseResources(Configuration.class.getClassLoader(), "default.conf");

        if (!Modes.testMode) {
            // User configuration will not be loaded in test mode
            String workspace = getWorkspace();
            File userConfFile = new File(workspace, DEFAULT_USER_CONFIG_FILENAME);
            if (userConfFile.exists()) {
                Config userConf = ConfigFactory.parseFile(userConfFile);
                conf = userConf.withFallback(conf);
            }
        }

        conf = conf.resolve();
        return new ConfigParser().parseAndCheck("greed", conf.getConfig("greed"), GreedConfig.class);
    }
}
