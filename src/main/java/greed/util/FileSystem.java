package greed.util;

import java.io.*;

/**
 * Greed is good! Cheers!
 */
public class FileSystem {
    private static final int NUM_BACKUPS = 3;
    private static final String BUILTIN_PREFIX = "builtin ";

    public static InputStream getInputStream(String resourcePath) throws FileNotFoundException {
        Log.i("Getting resource: " + resourcePath);
        if (resourcePath.startsWith(BUILTIN_PREFIX)) {
            resourcePath = Configuration.TEMPLATE_PATH + "/" + resourcePath.substring(BUILTIN_PREFIX.length());
            if (Debug.developmentMode) {
                resourcePath = Debug.getResourceDirectory() + resourcePath;
                return new FileInputStream(resourcePath);
            } else
                return FileSystem.class.getResourceAsStream(resourcePath);
        } else {
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
            Log.e("Write file error", e);
        }
    }

    public static boolean exists(String relativePath) {
        return new File(Configuration.getWorkspace() + "/" + relativePath).exists();
    }

    public static String getParentPath(String relativePath) {
        File f = new File(relativePath);
        return f.getParent();
    }
    public static File getRawFile(String relativePath) {
        File f = new File(Configuration.getWorkspace() + "/" + relativePath);
        try {
            return f.getCanonicalFile();
        } catch (IOException e) {
            // leave f untouched, possibly the file path does not exist, so
            // fixing the non-canonical path is not important
            return f;
        }
    }

    public static long getSize(String resourcePath) {
        File f = new File(Configuration.getWorkspace() + "/" + resourcePath);
        if (f.exists() && f.isFile())
            return f.length();
        return -1;
    }

    public static void backup(String relativePath) {
        String absolutePath = Configuration.getWorkspace() + "/" + relativePath;
        File file = new File(absolutePath);
        if (!file.exists()) return;

        Log.i("Backing up file " + relativePath);
        int i;
        for (i = 0; i < NUM_BACKUPS; ++i)
            if (!new File(absolutePath + ".bak." + i).exists()) break;
        if (i == NUM_BACKUPS) {
            new File(absolutePath + ".bak.0").delete();
            for (int j = 1; j < i; ++j)
                new File(absolutePath + ".bak." + j).renameTo(new File(absolutePath + ".bak." + (j - 1)));
            i = NUM_BACKUPS - 1;
        }
        Log.i("Renamed to " + relativePath + ".bak." + i);
        file.renameTo(new File(absolutePath + ".bak." + i));
    }
}
