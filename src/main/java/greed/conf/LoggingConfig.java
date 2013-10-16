package greed.conf;

import greed.conf.meta.Required;

/**
 * Greed is good! Cheers!
 */
public class LoggingConfig {
    @Required
    private String logLevel;

    @Required
    private boolean logToStderr;

    @Required
    private String logFolder;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isLogToStderr() {
        return logToStderr;
    }

    public void setLogToStderr(boolean logToStderr) {
        this.logToStderr = logToStderr;
    }

    public String getLogFolder() {
        return logFolder;
    }

    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }
}
