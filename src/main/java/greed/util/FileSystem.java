package greed.util;

import java.io.*;

/**
 * Greed is good! Cheers!
 */
public class FileSystem {
    public static InputStream getInputStream(String resourcePath) throws FileNotFoundException {
        Log.i("Getting resource: " + resourcePath);
        if (resourcePath.startsWith("res:")) {
            resourcePath = Configuration.getString(Configuration.Keys.JAR_RESOURCE) + resourcePath.substring(4);
            if (Debug.developmentMode) {
                resourcePath = Debug.getResourceDirectory() + resourcePath;
                return new FileInputStream(resourcePath);
            }
            else
                return FileSystem.class.getResourceAsStream(resourcePath);
        }
        else {
            return new FileInputStream(Configuration.getWorkspace() + "/" + resourcePath);
        }
    }

    /* Log is relied on this method, hence Log cannot be used in here */
    public static PrintWriter createWriter(String relativePath, boolean append) throws IOException {
        return new PrintWriter(new FileWriter(Configuration.getWorkspace() + "/" + relativePath, append));
    }

    /* Log is relied on this method, hence Log cannot be used in here */
    public static void createFolder(String relativePath) {
        File folder = new File(Configuration.getWorkspace() + "/" + relativePath);
        if (!folder.exists()) folder.mkdirs();
    }

    public static void writeFile(String relativePath, String content) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(Configuration.getWorkspace() + "/" + relativePath);
            writer.print(content);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(String relativePath) {
        return new File(Configuration.getWorkspace() + "/" + relativePath).exists();
    }

    public static void backup(String relativePath) {
        String absolutePath = Configuration.getWorkspace() + "/" + relativePath;
        File file = new File(absolutePath);
        if (!file.exists()) return;

        Log.i("Backing up file " + relativePath);
        int i;
        for (i = 0; i < 10; ++i)
            if (!new File(absolutePath + ".bak." + i).exists()) break;
        if (i == 10) {
            new File(absolutePath + ".bak.0").delete();
            for (int j = 1; j < i; ++j)
                new File(absolutePath + ".bak." + j).renameTo(new File(absolutePath + ".bak." + (j - 1)));
            i = 9;
        }
        Log.i("Renamed to " + relativePath + ".bak." + i);
        file.renameTo(new File(absolutePath + ".bak." + i));
    }
}
