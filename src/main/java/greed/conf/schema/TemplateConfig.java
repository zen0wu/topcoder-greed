package greed.conf.schema;

import java.util.HashMap;
import greed.conf.meta.*;
import greed.conf.parser.TemplateDependencyParser;
import greed.util.ResourcePath;
import greed.conf.parser.TemplateFileParser;

/**
 * Greed is good! Cheers!
 */
@ConfigObjectClass
public class TemplateConfig {
    public static enum OverwriteOptions {
        FORCE, BACKUP, SKIP
    }

    @Required
    @Parser(TemplateFileParser.class)
    private ResourcePath templateFile;

    @Optional
    private String outputKey;

    @Conflict(withField = { "outputFileName", "outputFileExtension" })
    @Optional
    @Stub
    private String outputFile;

    @Optional
    private String outputFileName;
    @Optional
    private String outputFileExtension;

    @Optional
    private OverwriteOptions overwrite = OverwriteOptions.BACKUP;

    @Optional
    private CommandConfig afterFileGen;

    @Optional
    private String[] transformers;

    @Optional
    @Parser(TemplateDependencyParser.class)
    private TemplateDependencyConfig.Dependency[] dependencies;

    @Optional
    @MapParam(value = String.class)
    private HashMap<String, String> options;
    
    public OverwriteOptions getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(OverwriteOptions overwrite) {
        this.overwrite = overwrite;
    }

    public String getOutputKey() {
        return outputKey;
    }

    public void setOutputKey(String outputKey) {
        this.outputKey = outputKey;
    }

    public String getOutputFile() {
        String name = getOutputFileName();
        String ext = getOutputFileExtension();
        StringBuilder fullName = new StringBuilder();
        if (name != null)
            fullName.append(name);
        if (ext != null && ext.length() > 0)
            fullName.append('.').append(ext);
        if (fullName.length() > 0)
            return fullName.toString();
        else
            return null;
    }

    public void setOutputFile(String outputFile) {
        int dot = outputFile.lastIndexOf(".");
        if (dot == -1) {
            setOutputFileName(outputFile);
            setOutputFileExtension("");
        }
        else {
            setOutputFileName(outputFile.substring(0, dot));
            setOutputFileExtension(outputFile.substring(dot + 1));
        }
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public void setOutputFileExtension(String outputFileExtension) {
        this.outputFileExtension = outputFileExtension;
    }

    public CommandConfig getAfterFileGen() {
        return afterFileGen;
    }

    public void setAfterFileGen(CommandConfig afterFileGen) {
        this.afterFileGen = afterFileGen;
    }

    public ResourcePath getTemplateFile() { return templateFile; }

    public void setTemplateFile(ResourcePath templateFile) { this.templateFile = templateFile; }

    public String[] getTransformers() {
        return transformers;
    }

    public void setTransformers(String[] transformers) {
        this.transformers = transformers;
    }
    
    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public TemplateDependencyConfig.Dependency[] getDependencies() { return dependencies; }

    public void setDependencies(TemplateDependencyConfig.Dependency[] dependencies) { this.dependencies = dependencies; }
}
