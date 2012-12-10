package greed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author WuCY
 */
public class AppInfo {
    private final static String APP_NAME = "Greed";
    private static String VERSION = null;

    public static String getAppName() {
        return APP_NAME;
    }

    public static String getVersion() {
        if (VERSION == null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("version")));
                VERSION = reader.readLine().trim();
                reader.close();
            }
            catch (IOException e) {
                VERSION = "UNKNOWN";
            }
        }
        return VERSION;
    }
}
