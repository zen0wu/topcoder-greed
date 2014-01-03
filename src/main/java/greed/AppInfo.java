package greed;

import greed.util.Modes;

import java.io.*;

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
                VERSION = Modes.devMode
                        ? readVersionFromStream(new FileInputStream("version"))
                        : readVersionFromStream(AppInfo.class.getResourceAsStream("/version"));
            } catch (FileNotFoundException e) {}
        }
        return VERSION;
    }

    private static String readVersionFromStream(InputStream is) {
        if (is == null) {
            return "[UNKNOWN]";
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            return reader.readLine().trim();
        }
        catch (IOException e) {}
        finally {
            if (reader != null)
                try { reader.close(); } catch (IOException e) {}
        }
        return "[UNKNOWN]";
    }

    public static void main(String[] args) {
        System.out.println(getVersion());
    }
}
