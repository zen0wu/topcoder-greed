package greed.conf.schema;

import greed.conf.meta.ConfigObjectClass;
import greed.conf.meta.Required;

/**
 * Greed is good! Cheers!
 */
@ConfigObjectClass
public class BackupConfig {
    @Required
    private String fileName;
    
    @Required
    private int fileCountLimit;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileCountLimit(int fileCountLimit) {
        this.fileCountLimit = fileCountLimit;
    }
    
    public int getFileCountLimit() {
        return this.fileCountLimit;
    }
}
