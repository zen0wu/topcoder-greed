package greed;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import greed.code.CodeByLine;
import greed.code.ConfigurableCodeTransformer;
import greed.code.LanguageManager;
import greed.code.transform.AppendingTransformer;
import greed.code.transform.CutBlockRemover;
import greed.code.transform.EmptyCutBlockCleaner;
import greed.code.transform.ContinuousBlankLineRemover;
import greed.conf.schema.CommandConfig;
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
            talkingWindow.showLine(String.format("Greetings from %s", AppInfo.getAppName()));
        } else {
            talkingWindow.showLine(String.format("Hello again :>"));
        }
        firstUsing = false;

        try {
            Utils.initialize();
        } catch (greed.conf.ConfigException e) {
            talkingWindow.error("Loading config error, saying \"" + e.getMessage() + "\", try fix it.");
            Log.e("Loading config error", e);
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
        if (currentLang == Language.VB) {
            talkingWindow.error("Unsupported language " + currentLang.toString());
            return;
        }
        currentProb = Convert.convertProblem(componentModel, currentLang);

        talkingWindow.showLine(String.format("Problem :  %s", currentProb.getName()));
        talkingWindow.showLine(String.format("Score   :  %d", currentProb.getScore()));
        generateCode(false);
    }

    public void generateCode(boolean forceOverride) {
        // Check whether workspace is set
        if (Configuration.getWorkspace() == null || "".equals(Configuration.getWorkspace())) {
            talkingWindow.setEnabled(false);
            talkingWindow.error("Workspace not configured, go set it!");
            Log.e("Workspace not configured");
            return;
        }

        talkingWindow.setEnabled(true);
        try {
            setProblem(currentContest, currentProb, currentLang, forceOverride);
        } catch (Throwable e) {
            talkingWindow.error("Set problem error, message says \"" + e.getMessage() + "\"");
            Log.e("Set problem error", e);
        }
    }
    
    private String renderedCodeRoot(GreedConfig config)
    {
        return TemplateEngine.render(config.getCodeRoot(), currentTemplateModel);
    }

    private void setProblem(Contest contest, Problem problem, Language language, boolean forceOverride) {
        GreedConfig config = Utils.getGreedConfig();
        LanguageConfig langConfig = config.getLanguage().get(Language.getName(language));

        // Initialize code transformers
        HashMap<String, ConfigurableCodeTransformer> codeTransformers = new HashMap<String, ConfigurableCodeTransformer>();
        for (ConfigurableCodeTransformer ccf: new ConfigurableCodeTransformer[] {
                new ContinuousBlankLineRemover(),
                new EmptyCutBlockCleaner(langConfig.getCutBegin(), langConfig.getCutEnd())
        }) {
            codeTransformers.put(ccf.getId(), ccf);
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

            talkingWindow.show(String.format("Generating template [" + templateName + "]"));
            // Generate code from templates
            String code;
            try {
                CodeByLine codeLines = CodeByLine.fromString(TemplateEngine.render(
                        FileSystem.getInputStream(template.getTemplateFile()),
                        currentTemplateModel
                ));

                if (template.getTransformers() != null) {
                    for (String transformerId: template.getTransformers()) {
                        if (codeTransformers.containsKey(transformerId)) {
                            codeLines = codeTransformers.get(transformerId).transform(codeLines);
                        }
                        else {
                            talkingWindow.indent();
                            talkingWindow.error("Unknown transformer \"" + transformerId + "\"");
                            talkingWindow.unindent();
                        }
                    }
                }

                code = codeLines.toString();
            } catch (FileNotFoundException e) {
                talkingWindow.indent();
                talkingWindow.error("Template file \"" + template.getTemplateFile() + "\" not found");
                talkingWindow.unindent();
                continue;
            }

            // Output to model
            if (template.getOutputKey() != null) {
                currentTemplateModel.put(template.getOutputKey(), code);
            }

            // Output to file
            if (template.getOutputFile() != null) {
                String filePath = renderedCodeRoot(config) + "/" +
                        TemplateEngine.render(template.getOutputFile(), currentTemplateModel);
                String fileFolder = FileSystem.getParentPath(filePath);
                if (!FileSystem.exists(fileFolder)) {
                    FileSystem.createFolder(fileFolder);
                }

                boolean exists = FileSystem.exists(filePath);
                boolean override = forceOverride || template.isOverride();
                talkingWindow.show(" -> " + filePath);
                if (exists && !override) {
                    talkingWindow.showLine(" (skipped)");
                    continue;
                }
                if (exists) {
                    if (FileSystem.getSize(filePath) == code.length()) {
                        talkingWindow.show(" (skipped, same size)");
                    } else {
                        talkingWindow.show(" (overwrite)");
                        FileSystem.backup(filePath); // Backup the old files
                        FileSystem.writeFile(filePath, code);
                    }
                }
                else
                    FileSystem.writeFile(filePath, code);

                if (template.getAfterFileGen() != null) {
                    CommandConfig afterGen = template.getAfterFileGen();
                    String[] commands = new String[afterGen.getArguments().length + 1];
                    commands[0] = afterGen.getExecute();
                    currentTemplateModel.put("GeneratedFileName", new java.io.File(filePath).getName());
                    currentTemplateModel.put("GeneratedFilePath", FileSystem.getRawFile(filePath).getPath());
                    for (int i = 1; i < commands.length; ++i) {
                        commands[i] = TemplateEngine.render(afterGen.getArguments()[i - 1], currentTemplateModel);
                    }

                    talkingWindow.showLine("");
                    talkingWindow.indent();
                    talkingWindow.showLine("After generation action: ");
                    talkingWindow.indent();
                    talkingWindow.showLine(String.format("(%s)$ %s", fileFolder, StringUtil.join(commands, " ")));
                    talkingWindow.show("Exit status: " + ExternalSystem.runExternalCommand(FileSystem.getRawFile(fileFolder), commands));
                    talkingWindow.unindent();
                    talkingWindow.unindent();
                }
            }
            talkingWindow.showLine("");
        }

        talkingWindow.showLine("All set, good luck!");
        talkingWindow.showLine("");
    }

    public String getSource() {
        GreedConfig config = Utils.getGreedConfig();
        LanguageConfig langConfig = config.getLanguage().get(Language.getName(currentLang));
        String filePath = renderedCodeRoot(config) + "/" +
                TemplateEngine.render(langConfig.getTemplateDef().get(langConfig.getSubmitTemplate()).getOutputFile(), currentTemplateModel);

        talkingWindow.showLine("Getting source code from " + filePath);
        talkingWindow.indent();
        String result = "";
        if (!FileSystem.exists(filePath)) {
            talkingWindow.error("Source code file doesn't exist");
        }
        else {
            try {
                CodeByLine code = CodeByLine.fromInputStream(FileSystem.getInputStream(filePath));

                if (LanguageManager.getInstance().getPostTransformer(currentLang) != null)
                    code = LanguageManager.getInstance().getPostTransformer(currentLang).transform(code);

                code = new CutBlockRemover(langConfig.getCutBegin(), langConfig.getCutEnd()).transform(code);
                code = new AppendingTransformer(getSignature()).transform(code);

                result = code.toString();
            } catch (IOException e) {
                talkingWindow.error("Cannot fetch source code, message says \"" + e.getMessage() + "\"");
                Log.e("Cannot fetch source code", e);
            }
        }
        talkingWindow.unindent();
        return result;
    }
}
