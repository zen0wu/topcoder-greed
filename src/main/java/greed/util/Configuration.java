package greed.util;

import com.topcoder.client.contestApplet.common.LocalPreferences;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import greed.model.Language;

import java.io.File;
import java.io.IOException;

/**
 * Greed is good! Cheers!
 */
@SuppressWarnings("unused")
public class Configuration {
    // Workspace related
    private static LocalPreferences pref = LocalPreferences.getInstance();

    private static final String GREED_WORKSPACE = "greed.workspace";

    public static boolean workspaceSet() {
        String workspace = getWorkspace();
        return workspace != null && workspace.length() > 0;
    }

    public static String getWorkspace() {
        return pref.getProperty(GREED_WORKSPACE);
    }

    public static void setWorkspace(String workspace) {
        pref.setProperty(GREED_WORKSPACE, workspace);
        try {
            pref.savePreferences();
        } catch (IOException e) {
            Log.e("Save working space error", e);
        }
    }

    // Configuration keys
    public static class Keys {
        public static final String JAR_RESOURCE = "greed.reserved.jarResourcePath";

        public static final String CODE_ROOT = "greed.codeRoot";

        public static final String FILE_NAME_PATTERN = "greed.templates.fileNamePattern";
        public static final String UNIT_TEST_FILE_NAME_PATTERN = "greed.templates.unitTestFileNamePattern";
        public static final String PATH_PATTERN = "greed.templates.pathPattern";

        public static final String OVERRIDE = "greed.override";
        public static final String LOG_LEVEL = "greed.logLevel";
        public static final String LOG_TO_STDERR = "greed.logToStderr";
        public static final String LOG_FOLDER = "greed.logFolder";

        public static final String RECORD_RUNTIME = "greed.test.recordRuntime";
        public static final String RECORD_SCORE = "greed.test.recordScore";
        public static final String UNIT_TEST = "greed.test.unitTest";

        public static String getTemplateKey(Language language) {
            return "greed.templates." + Language.getName(language);
        }

        public static final String SUBKEY_TEMPLATE_FILE = "tmplFile";
        public static final String SUBKEY_TEST_TEMPLATE_FILE = "testTmplFile";
        public static final String SUBKEY_UNIT_TEST_TEMPLATE_FILE = "unitTestTmplFile";
        public static final String SUBKEY_EXTENSION = "extension";
        public static final String SUBKEY_CUTBEGIN = "cutBegin";
        public static final String SUBKEY_CUTEND = "cutEnd";
        public static final String SUBKEY_LONG_TYPE_NAME = "longTypeName";
    }

    private static final String DEFAULT_USER_CONFIG_FILENAME = "greed.conf";
    private static final String RESERVED_CONFPATH = "greed.reserved";

    private static Config conf = null;

    private static void lazyInit() {
        if (conf != null) return;

        if (Debug.developmentMode) {
            conf = ConfigFactory.parseFile(new File(Debug.getResourceDirectory() + "/default.conf"));
        } else {
            conf = ConfigFactory.parseURL(Configuration.class.getResource("/default.conf"));
        }

        String workspace = getWorkspace();
        File userConfFile = new File(workspace, DEFAULT_USER_CONFIG_FILENAME);
        if (userConfFile.exists()) {
            Config userConf = ConfigFactory.parseFile(userConfFile);
            conf = userConf.withoutPath(RESERVED_CONFPATH).withFallback(conf);
        }
    }

    public static String getString(String key) {
        lazyInit();
        return conf.getString(key);
    }

    public static boolean getBoolean(String key) {
        lazyInit();
        return conf.getBoolean(key);
    }

    public static int getInt(String key) {
        lazyInit();
        return conf.getInt(key);
    }

    public static Config getLanguageConfig(Language lang) {
        lazyInit();
        return conf.getConfig(Keys.getTemplateKey(lang));
    }

    public static Config getConfig() {
        lazyInit();
        return conf;
    }

    public static void reload() {
        conf = null;
        lazyInit();
    }
}
