package greed.conf.schema;

import greed.conf.meta.ConfigObjectClass;
import greed.conf.meta.Required;

/**
 * Greed is good! Cheers!
 */
@ConfigObjectClass
public class LoggingConfig {
    public static enum LoggingLevel {
        ALL(-1), DEBUG(0), INFO(1), WARN(2), ERROR(3), OFF(4);

        public final int value;

        private LoggingLevel(int v) { this.value = v; }
    }

    @Required
    private LoggingLevel logLevel;

    @Required
    private boolean logToStderr;

    @Required
    private String logFolder;

    public LoggingLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LoggingLevel logLevel) {
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
