package greed.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressWarnings("unused")
public class Log {
    private static final int STACK_TRACE_BACKDEPTH = 4;
    private static final String[] LEVEL_NAMES = new String[] { "DEBUG", "INFO", "WARN", "ERROR" };
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);

    private static int minimalLoggingLevel = 0;
    private static PrintWriter logger = null;
    private static int logCount = 0;
    private static boolean logToErr = false;

    private static void lazyInit() {
        String levelName = Configuration.getString(Configuration.Keys.LOG_LEVEL);
        minimalLoggingLevel = LEVEL_NAMES.length + 1;
        if ("all".equals(levelName.toLowerCase())) minimalLoggingLevel = 0;
        else
            for (int i = 0; i < 4; ++i) if (LEVEL_NAMES[i].equals(levelName.toUpperCase())) {
                minimalLoggingLevel = i;
                break;
            }
        // If logging is enabled and workspace exists
        if (minimalLoggingLevel < LEVEL_NAMES.length && Configuration.workspaceSet()) {
            logToErr = Configuration.getBoolean(Configuration.Keys.LOG_TO_STDERR);
            // Create logging folder
            String logFolder = Configuration.getString(Configuration.Keys.LOG_FOLDER);
            FileSystem.createFolder(logFolder);

            int month = GregorianCalendar.getInstance().get(Calendar.MONTH);
            int day = GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH);
            try {
                logger = FileSystem.createWriter(logFolder + "/greed-" + month + "-" + day + ".log", true);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private static void doLog(int level, String message) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[STACK_TRACE_BACKDEPTH];
        String dateString = DATE_FORMAT.format(new Date());
        String prefix = "    ";
        if (level >= 0) prefix = trace.getClassName().substring(trace.getClassName().lastIndexOf(".") + 1) + "[" + LEVEL_NAMES[level] + "]:";
        String log = String.format("[%s] [%s.%s@line %d] %s %s",
                dateString, trace.getClassName(), trace.getMethodName(), trace.getLineNumber(),
                prefix, message);
        if (logger != null) {
            logger.println(log);
            if (logCount++ % 10 == 0) logger.flush();
        }
        if (logToErr) System.err.println(log);
    }

    private static void checkLoggingLevel(int level, String message) {
        lazyInit();
        if (level >= minimalLoggingLevel)
            doLog(level, message);
    }

    private static void checkLoggingLevel(int level, String message, Throwable e) {
        lazyInit();
        if (level >= minimalLoggingLevel) {
            doLog(level, String.format("%s, with an %s", message, e.toString()));
            for (int i = 0; i < Math.min(15, e.getStackTrace().length); ++i)
                doLog(-1, "    " + e.getStackTrace()[i].toString());
            if (e.getStackTrace().length > 15)
                doLog(-1, "    " + " and " + (e.getStackTrace().length - 15) + " more ...");
        }
    }

    public static void d(String message) {
        checkLoggingLevel(0, message);
    }

    public static void d(String message, Throwable e) {
        checkLoggingLevel(0, message, e);
    }

    public static void i(String message) {
        checkLoggingLevel(1, message);
    }

    public static void i(String message, Throwable e) {
        checkLoggingLevel(1, message, e);
    }

    public static void w(String message) {
        checkLoggingLevel(2, message);
    }

    public static void w(String message, Throwable e) {
        checkLoggingLevel(2, message, e);
    }

    public static void e(String message) {
        checkLoggingLevel(3, message);
    }

    public static void e(String message, Throwable e) {
        checkLoggingLevel(3, message, e);
    }
}
