package greed.util;

/**
 * Greed is good! Cheers!
 */
public class Modes {
    public static final boolean devMode = System.getProperty("developmentMode") != null && Boolean.parseBoolean(System.getProperty("developmentMode"));
    public static final boolean jarMode = Modes.class.getResource("Modes.class").toString().startsWith("jar:");
    public static final boolean testMode = !devMode && !jarMode;
}
