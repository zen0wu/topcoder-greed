package greed.conf.schema;

import greed.conf.meta.MapParam;
import greed.conf.meta.Required;

import java.util.HashMap;

/**
 * Greed is good! Cheers!
 */
public class GreedConfig {
    @Required
    private String codeRoot;

    @Required
    private LoggingConfig logging;

    @Required
    @MapParam(value = LanguageConfig.class)
    private HashMap<String, LanguageConfig> language;

    public String getCodeRoot() {
        return codeRoot;
    }

    public void setCodeRoot(String codeRoot) {
        this.codeRoot = codeRoot;
    }

    public LoggingConfig getLogging() {
        return logging;
    }

    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }

    public HashMap<String, LanguageConfig> getLanguage() {
        return language;
    }

    public void setLanguage(HashMap<String, LanguageConfig> language) {
        this.language = language;
    }
}
