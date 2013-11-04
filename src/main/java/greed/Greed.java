package greed;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import greed.code.CodeByLine;
import greed.code.LanguageManager;
import greed.code.transform.AppendingTransformer;
import greed.code.transform.CutBlockRemover;
import greed.code.transform.EmptyCutBlockCleaner;
import greed.code.transform.ContinuousBlankLineRemover;
import greed.conf.schema.GreedConfig;
import greed.conf.schema.LanguageConfig;
import greed.conf.schema.TemplateConfig;
import greed.model.*;
import greed.template.TemplateEngine;
import greed.ui.ConfigurationDialog;
import greed.ui.GreedEditorPanel;
import greed.util.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Greed is good! Cheers!
 */
@SuppressWarnings("unused")
public class Greed {
    private Language currentLang;
    private Problem currentProb;
    private Contest currentContest;
    private HashMap<String, Object> currentTemplateModel;

    private GreedEditorPanel talkingWindow;
    private boolean firstUsing;

    public Greed() {
        // Entrance of all program
        Log.i("Create Greed Plugin Instance");
        this.talkingWindow = new GreedEditorPanel(this);
        this.firstUsing = true;
    }

    // Greed signature in the code
    public String getSignature() {
        return String.format("%s Powered by %s %s",
                LanguageManager.getInstance().getTrait(currentLang).getCommentPrefix(), AppInfo.getAppName(), AppInfo.getVersion());
    }

    // Cache the editor
    public boolean isCacheable() {
        return !Debug.developmentMode;
    }

    // Called when open the coding frame
    // Like FileEdit, a log window is used
    public JPanel getEditorPanel() {
        return talkingWindow;
    }

    // Ignore the given source code
    public void setSource(String source) {
    }

    public void startUsing() {
        Log.i("Start using called");
        talkingWindow.clear();
        if (firstUsing) {
            talkingWindow.say(String.format("Hi, this is %s.", AppInfo.getAppName()));
        } else {
            talkingWindow.say(String.format("So we meet again :>"));
        }
        firstUsing = false;

        try {
            Utils.initialize();
        } catch (greed.conf.ConfigException e) {
            Log.e("Exception while loading config", e);
            talkingWindow.say("Something wrong when loading config saying \"" + e.getMessage() + "\", try fix it.");
        }
    }

    public void stopUsing() {
        Log.i("Stop using called");
    }

    public void configure() {
        new ConfigurationDialog().setVisible(true);
    }

    public void setProblemComponent(ProblemComponentModel componentModel, com.topcoder.shared.language.Language language, Renderer renderer) {
        currentContest = Convert.convertContest(componentModel);
        currentLang = Convert.convertLanguage(language);
        currentProb = Convert.convertProblem(componentModel, currentLang);

        talkingWindow.say("Hmm, it's a problem with " + currentProb.getScore() + " points. Good choice!");
        generateCode(false);
    }

    public void generateCode(boolean forceOverride) {
        // Check whether workspace is set
        if (Configuration.getWorkspace() == null || "".equals(Configuration.getWorkspace())) {
            talkingWindow.setEnabled(false);
            talkingWindow.say("It seems that you haven't set your workspace, go set it!");
            Log.e("Workspace not set");
            return;
        }

        talkingWindow.setEnabled(true);
        try {
            setProblem(currentContest, currentProb, currentLang, forceOverride);
        } catch (Throwable e) {
            talkingWindow.say("Oops, something wrong! It says \"" + e.getMessage() + "\"");
            talkingWindow.say("Please see the logs for details.");
            Log.e("Set problem failed", e);
        }
    }

    private void setProblem(Contest contest, Problem problem, Language language, boolean forceOverride) {
        GreedConfig config = Utils.getGreedConfig();
        LanguageConfig langConfig = config.getLanguage().get(Language.getName(language));
        if (langConfig == null) {
            talkingWindow.say("Unsupported language " + language.toString());
            return;
        }

        // Create model map
        currentTemplateModel = new HashMap<String, Object>();
        currentTemplateModel.put("Contest", contest);
        currentTemplateModel.put("Problem", problem);
        // Bind problem template model
        currentTemplateModel.put("ClassName", problem.getClassName());
        currentTemplateModel.put("Method", problem.getMethod());
        currentTemplateModel.put("Examples", problem.getTestcases());
        currentTemplateModel.put("NumOfExamples", problem.getTestcases().length);
        boolean useArray = problem.getMethod().getReturnType().isArray();
        currentTemplateModel.put("ReturnsArray", useArray);
        for (Param param : problem.getMethod().getParams()) useArray |= param.getType().isArray();
        currentTemplateModel.put("HasArray", useArray);

        boolean useString = problem.getMethod().getReturnType().isString();
        currentTemplateModel.put("ReturnsString", useString);
        for (Param param : problem.getMethod().getParams()) useString |= param.getType().isString();        
        currentTemplateModel.put("HasString", useString);
        currentTemplateModel.put("CreateTime", System.currentTimeMillis() / 1000);
        currentTemplateModel.put("CutBegin", langConfig.getCutBegin());
        currentTemplateModel.put("CutEnd", langConfig.getCutEnd());

        TemplateEngine.switchLanguage(language);

        // Generate templates
        for (String templateName : langConfig.getTemplates()) {
            TemplateConfig template = langConfig.getTemplateDef().get(templateName);

            talkingWindow.say(String.format("Generating template [" + templateName + "]"));
            // Generate code from templates
            String code;
            try {
                CodeByLine codeLines = CodeByLine.fromString(TemplateEngine.render(
                        FileSystem.getInputStream(template.getTemplateFile()),
                        currentTemplateModel
                ));
                codeLines = new EmptyCutBlockCleaner(langConfig.getCutBegin(), langConfig.getCutEnd()).transform(codeLines);
                codeLines = new ContinuousBlankLineRemover().transform(codeLines);
                code = codeLines.toString();
            } catch (FileNotFoundException e) {
                talkingWindow.say("Template file \"" + template.getTemplateFile() + "\" not found");
                continue;
            }

            // Output to model
            if (template.getOutputKey() != null) {
                currentTemplateModel.put(template.getOutputKey(), code);
            }

            // Output to file
            if (template.getOutputFile() != null) {
                String filePath = config.getCodeRoot() + "/" +
                        TemplateEngine.render(template.getOutputFile(), currentTemplateModel);
                String fileFolder = FileSystem.getParentPath(filePath);
                if (!FileSystem.exists(fileFolder)) {
                    talkingWindow.say("Creating folder " + fileFolder);
                    FileSystem.createFolder(fileFolder);
                }

                boolean exists = FileSystem.exists(filePath);
                boolean override = forceOverride || template.isOverride();
                talkingWindow.say("Writing to " + filePath);
                if (exists && !override) {
                    talkingWindow.say("Skip due to override policy");
                    continue;
                }
                writeFileWithBackup(filePath, code, exists);
            }

            // TODO: After gen hook
        }

        talkingWindow.say("All set, good luck!");
        talkingWindow.say("");
    }

    public String getSource() {
        GreedConfig config = Utils.getGreedConfig();
        LanguageConfig langConfig = config.getLanguage().get(Language.getName(currentLang));

        String filePath = config.getCodeRoot() + "/" +
                TemplateEngine.render(langConfig.getTemplateDef().get(langConfig.getSubmitTemplate()).getOutputFile(), currentTemplateModel);

        talkingWindow.say("Submitting " + filePath);
        if (!FileSystem.exists(filePath)) {
            talkingWindow.say("Cannot found your source code");
            return "";
        }

        try {
            CodeByLine code = CodeByLine.fromInputStream(FileSystem.getInputStream(filePath));

            if (LanguageManager.getInstance().getPostTransformer(currentLang) != null)
                code = LanguageManager.getInstance().getPostTransformer(currentLang).transform(code);

            code = new CutBlockRemover(langConfig.getCutBegin(), langConfig.getCutEnd()).transform(code);
            code = new AppendingTransformer(getSignature()).transform(code);

            return code.toString();
        } catch (IOException e) {
            talkingWindow.say("Err... Cannot fetch your source code. Please check the logs, and make sure your source code is present");
            Log.e("Error getting the source", e);
            return "";
        }
    }

    private void writeFileWithBackup(String path, String content, boolean exists) {
        if (content != null) {
            if (exists) {
                talkingWindow.say("Overriding \"" + path + "\", old files will be renamed");
                if (FileSystem.getSize(path) == content.length()) {
                    talkingWindow.say("Seems the current file is the same as the code to write, skipped");
                } else
                    FileSystem.backup(path); // Backup the old files
            }

            FileSystem.writeFile(path, content);
        }
    }
}
