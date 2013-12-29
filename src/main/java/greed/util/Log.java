package greed.util;

import greed.conf.schema.LoggingConfig;
import greed.conf.schema.LoggingConfig.LoggingLevel;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
@SuppressWarnings("unused")
public class Log {
    private static final int STACK_TRACE_BACKDEPTH = 4;
    private static final int STACK_TRACE_MAX_DEPTH = 10;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);

    private static LoggingLevel minimalLoggingLevel = LoggingLevel.INFO;
    private static PrintWriter logger = null;
    private static int logCount = 0;
    // Default set to true to allow pre-initialized logging to StdErr
    private static boolean logToErr = true;

    static void initialize(LoggingConfig config) {
        minimalLoggingLevel = config.getLogLevel();
        logToErr = config.isLogToStderr();
        // If logging is enabled and workspace exists
        if (minimalLoggingLevel.value < LoggingLevel.OFF.value && Configuration.workspaceSet()) {
            // Create logging folder
            String logFolder = config.getLogFolder();
            if (logFolder == null) {
                throw new IllegalArgumentException("Log folder cannot be null");
            }
            FileSystem.createFolder(logFolder);

            int month = GregorianCalendar.getInstance().get(Calendar.MONTH) + 1;
            int day = GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH);
            try {
                logger = FileSystem.createWriter(logFolder + "/greed-" + month + "-" + day + ".log", true);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private static void doLog(LoggingLevel level, String message) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[STACK_TRACE_BACKDEPTH];
        String dateString = DATE_FORMAT.format(new Date());
        String prefix = "    ";
        if (level != LoggingLevel.ALL)
            prefix = trace.getClassName().substring(trace.getClassName().lastIndexOf(".") + 1) + "[" + level.toString() + "]:";
        String log = String.format("[%s] [%s.%s@L%d] %s %s",
                dateString, trace.getClassName(), trace.getMethodName(), trace.getLineNumber(),
                prefix, message);
        if (logger != null) {
            logger.println(log);
            if (logCount++ % 10 == 0) logger.flush();
        }
        if (logToErr) System.err.println(log);
    }

    private static void checkLoggingLevel(LoggingLevel level, String message) {
        if (level.value >= minimalLoggingLevel.value)
            doLog(level, message);
    }

    private static void checkLoggingLevel(LoggingLevel level, String message, Throwable e) {
        if (level.value >= minimalLoggingLevel.value) {
            doLog(level, String.format("%s, with an %s", message, e.toString()));
            for (int i = 0; i < Math.min(STACK_TRACE_MAX_DEPTH, e.getStackTrace().length); ++i)
                doLog(LoggingLevel.ALL, "    " + e.getStackTrace()[i].toString());
            if (e.getStackTrace().length > STACK_TRACE_MAX_DEPTH)
                doLog(LoggingLevel.ALL, "    " + " and " + (e.getStackTrace().length - STACK_TRACE_MAX_DEPTH) + " more ...");
        }
    }

    public static void d(String message) {
        checkLoggingLevel(LoggingLevel.DEBUG, message);
    }

    public static void d(String message, Throwable e) {
        checkLoggingLevel(LoggingLevel.DEBUG, message, e);
    }

    public static void i(String message) {
        checkLoggingLevel(LoggingLevel.INFO, message);
    }

    public static void i(String message, Throwable e) {
        checkLoggingLevel(LoggingLevel.INFO, message, e);
    }

    public static void w(String message) {
        checkLoggingLevel(LoggingLevel.WARN, message);
    }

    public static void w(String message, Throwable e) {
        checkLoggingLevel(LoggingLevel.WARN, message, e);
    }

    public static void e(String message) {
        checkLoggingLevel(LoggingLevel.ERROR, message);
    }

    public static void e(String message, Throwable e) {
        checkLoggingLevel(LoggingLevel.ERROR, message, e);
    }
}
