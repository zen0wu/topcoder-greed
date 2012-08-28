package greed.util;

/**
 * Greed is good! Cheers!
 */
public class Debug {
    public static final boolean developmentMode = System.getProperty("developmentMode") != null && Boolean.parseBoolean(System.getProperty("developmentMode"));

    public static String getWorkingDirectory() {
        return System.getProperty("projectDir");
    }

    public static String getResourceDirectory() {
        return getWorkingDirectory() + "/src/main/resources";
    }
}
