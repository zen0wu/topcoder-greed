package greed.util;

public class Debug {
    public static final boolean developmentMode = System.getProperty("developmentMode") != null ? Boolean.parseBoolean(System.getProperty("developmentMode")) : false;

    public static String getWorkingDirectory() {
        return System.getProperty("projectDir");
    }

    public static String getResourceDirectory() {
        return getWorkingDirectory() + "/src/main/resources";
    }
}
