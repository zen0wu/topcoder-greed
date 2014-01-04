package greed.util;

import greed.template.TemplateEngine;

import java.io.*;
import java.util.HashMap;
/**
 * Greed is good! Cheers!
 */
public class FileSystem {
    public static InputStream getResource(ResourcePath resourcePath) throws FileNotFoundException {
        Log.i("Getting resource: " + resourcePath.getRelativePath());
        if (resourcePath.isInternal()) {
            String relativePath = resourcePath.getRelativePath();
            InputStream is = FileSystem.class.getResourceAsStream(relativePath);
            if (is == null)
                throw new FileNotFoundException(relativePath);
            return is;
        } else {
            return new FileInputStream(Configuration.getWorkspace() + "/" + resourcePath.getRelativePath());
        }
    }

    public static PrintWriter createWriter(String relativePath, boolean append) throws IOException {
        return new PrintWriter(new FileWriter(Configuration.getWorkspace() + "/" + relativePath, append));
    }

    public static void createFolder(String relativePath) {
        Log.i("Create folder [" + relativePath + "]");
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

    public static boolean exists(ResourcePath resourcePath) {
        if (resourcePath.isInternal()) {
            String relativePath = resourcePath.getRelativePath();
            InputStream is = FileSystem.class.getResourceAsStream(relativePath);
            return is != null;
        } else {
            return exists(resourcePath.getRelativePath());
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

    private static File getBackupFile(File file, int num)
    {
        String name = file.getName();
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("GeneratedFileName", name);
        model.put("BackupNumber", num);
        greed.conf.schema.BackupConfig bc = Utils.getGreedConfig().getBackup();
        String newname = TemplateEngine.getBareEngine().render( bc.getFileName(), model );
        File newfile = new File( file.getParentFile(), newname);
        if ( ! newfile.getParentFile().exists() ) {
            newfile.getParentFile().mkdirs();
        }
        return newfile;
    }
    
    public static void backup(String relativePath) {
        String absolutePath = Configuration.getWorkspace() + "/" + relativePath;
        File file = new File(absolutePath);
        if (!file.exists()) return;

        Log.i("Backing up file " + relativePath);
        int limit = Utils.getGreedConfig().getBackup().getFileCountLimit();
        int i;
        for (i = 0; i < limit; ++i)
            if (! getBackupFile(file, i).exists()) break;
        if (i == limit) {
            Log.i("Deleting " + getBackupFile(file, 0) );
            getBackupFile(file, 0).delete();
            for (int j = 1; j < i; ++j) {
                Log.i( getBackupFile(file, j) + " -> " + getBackupFile(file, j-1));
                getBackupFile(file, j).renameTo(getBackupFile(file, j-1));
            }
            i = limit - 1;
        }
        Log.i(absolutePath + " -> " + getBackupFile(file, i));
        file.renameTo(getBackupFile(file, i));
    }
    
    public static String readStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buf = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buf.append(line);
                buf.append("\n");
            }
            reader.close();
            stream.close();
        } catch (IOException e) {
            Log.e("IOException while reading stream", e);
            return null;
        }
        return buf.toString();
    }

}
