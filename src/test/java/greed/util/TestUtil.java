package greed.util;

/**
 * Greed is good! Cheers!
 */
public class TestUtil {
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (osName == null) return false;	// won't happen, but avoid NPE ...
        return osName.contains("win") || osName.contains("Windows");
    }
}
