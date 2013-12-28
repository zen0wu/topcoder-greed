package greed;

import greed.code.CodeByLine;
import greed.code.ConfigurableCodeTransformer;
import greed.code.LanguageManager;
import greed.code.transform.AppendingTransformer;
import greed.code.transform.ContinuousBlankLineRemover;
import greed.code.transform.CutBlockRemover;
import greed.code.transform.EmptyCutBlockCleaner;
import greed.conf.schema.*;
import greed.model.Contest;
import greed.model.Convert;
import greed.model.Language;
import greed.model.Param;
import greed.model.Problem;
import greed.template.TemplateEngine;
import greed.ui.ConfigurationDialog;
import greed.ui.GreedEditorPanel;
import greed.util.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;

/**
 * Greed is good! Cheers!
 */
@SuppressWarnings("unused")
public class Greed {
    private Language currentLang;
    private Problem currentProb;
    private Contest currentContest;
    private HashMap<String, Object> currentModel;

    private GreedEditorPanel talkingWindow;
    private boolean initialized;

    public Greed() {
        // Entrance of all program
        Log.i("Greed Plugin");
        this.talkingWindow = new GreedEditorPanel(this);
        this.initialized = false;
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

    public void initialize() {
        try {
            talkingWindow.show(initialized ? "Reinitializing..." : "Initializing...");
            initialized = false;
            Utils.initialize();
            initialized = true;
            talkingWindow.showLine(" done");
            talkingWindow.showLine("");
        } catch (greed.conf.ConfigException e) {
            talkingWindow.error("  Config error: " + e.getMessage());
            Log.e("Loading config error", e);
        } catch (Throwable e) {
            talkingWindow.error("  Fatal error: " + e.getMessage());
            Log.e("Initialization error", e);
        }
    }

    public void startUsing() {
        Log.d("Start using");
        talkingWindow.clear();
        if (!initialized) {
            talkingWindow.showLine(String.format("Greetings from %s", AppInfo.getAppName()));
            initialize();
        } else {
            talkingWindow.showLine(String.format("Hello again :>"));
        }
    }

    public void stopUsing() {
        Log.d("Stop using");
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

        if (!initialized)
            return;

        generateCode(false);
    }

    public void generateCode(boolean regen) {
        // Check whether workspace is set
        if (Configuration.getWorkspace() == null || "".equals(Configuration.getWorkspace())) {
            talkingWindow.setEnabled(false);
            talkingWindow.error("Workspace not configured, go set it!");
            Log.e("Workspace not configured");
            return;
        }

        talkingWindow.setEnabled(true);
        try {
            talkingWindow.showLine(String.format("Problem :  %s", currentProb.getName()));
            talkingWindow.showLine(String.format("Score   :  %d", currentProb.getScore()));

            talkingWindow.showLine(regen ? "Regenerating code..." : "Generating code...");
            talkingWindow.indent();
            setProblem(currentContest, currentProb, currentLang, regen);
        } catch (Throwable e) {
            talkingWindow.error("Error: " + e.getMessage());
            Log.e("Set problem error", e);
        } finally {
            talkingWindow.unindent();
        }
    }

    private String renderedCodeRoot(GreedConfig config)
    {
        return TemplateEngine.render(config.getCodeRoot(), currentModel);
    }

    @SuppressWarnings("unchecked")
    private void setProblem(Contest contest, Problem problem, Language language, boolean regen) {
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
        currentModel = new HashMap<String, Object>();
        currentModel.put("Contest", contest);
        currentModel.put("Problem", problem);
        currentModel.put("ClassName", problem.getClassName());
        currentModel.put("Method", problem.getMethod());

        // Bind problem template model
        HashMap<String, Object> sharedModel = new HashMap<String, Object>(currentModel);
        sharedModel.put("Examples", problem.getTestcases());
        sharedModel.put("NumOfExamples", problem.getTestcases().length);
        boolean useArray = problem.getMethod().getReturnType().isArray();
        sharedModel.put("ReturnsArray", useArray);
        for (Param param : problem.getMethod().getParams()) useArray |= param.getType().isArray();
        sharedModel.put("HasArray", useArray);

        boolean useString = problem.getMethod().getReturnType().isString();
        sharedModel.put("ReturnsString", useString);
        for (Param param : problem.getMethod().getParams()) useString |= param.getType().isString();
        sharedModel.put("HasString", useString);
        sharedModel.put("CreateTime", System.currentTimeMillis() / 1000);
        sharedModel.put("CutBegin", langConfig.getCutBegin());
        sharedModel.put("CutEnd", langConfig.getCutEnd());

        // Switch language
        TemplateEngine.switchLanguage(language);

        // Validate template definitions and calculate order
        ArrayList<String> templateOrder;
        {
            ArrayList<String> templates = new ArrayList<String>();
            HashSet<String> templateSet = new HashSet<String>();
            // Find all the templates needed by hard constraint (direct template dependency)
            for (String templateName: langConfig.getTemplates()) {
                if (!langConfig.getTemplateDef().containsKey(templateName)) {
                    talkingWindow.error("Unknown template [" + templateName + "] (ignored)");
                    continue;
                }
                templates.add(templateName);
                templateSet.add(templateName);
            }
            for (int i = 0; i < templates.size(); ++i) {
                String template = templates.get(i);
                TemplateConfig templateConfig = langConfig.getTemplateDef().get(template);
                if (templateConfig.getDependencies() != null) {
                    for (TemplateDependencyConfig.Dependency dep: templateConfig.getDependencies()) {
                        if (dep instanceof TemplateDependencyConfig.TemplateDependency) {
                            String depTemplate = ((TemplateDependencyConfig.TemplateDependency)dep).getTemplate();
                            if (!templateSet.contains(depTemplate)) {
                                templateSet.add(depTemplate);
                                templates.add(depTemplate);
                            }
                        }
                    }
                }
            }
            // Queue the order
            templateOrder = new ArrayList<String>();
            HashSet<String> hasKeys = new HashSet<String>();
            HashSet<String> hasTemplates = new HashSet<String>();
            while (!templateSet.isEmpty()) {
                String selected = null;
                for (String template: templateSet) {
                    boolean independent = true;
                    TemplateConfig templateConfig = langConfig.getTemplateDef().get(template);
                    if (templateConfig.getDependencies() != null) {
                        for (TemplateDependencyConfig.Dependency dep: templateConfig.getDependencies()) {
                            if (!checkDependency(dep, hasKeys, hasTemplates)) {
                                independent = false;
                                break;
                            }
                        }
                    }
                    if (independent) {
                        selected = template;
                        break;
                    }
                }
                if (selected == null)
                    break;
                templateSet.remove(selected);
                templateOrder.add(selected);
                hasTemplates.add(selected);
                String key = langConfig.getTemplateDef().get(selected).getOutputKey();
                if (key != null)
                    hasKeys.add(key);
            }
            if (!templateSet.isEmpty())
                templateOrder = null;
        }
        if (templateOrder == null) {
            talkingWindow.error("Cannot figure out template generation order");
            return;
        }

        HashMap<String, Object> dependencyModel = new HashMap<String, Object>();
        sharedModel.put("Dependencies", dependencyModel);
        // Generate templates
        for (String templateName : templateOrder) {
            talkingWindow.show(String.format("Generating template [" + templateName + "]"));

            TemplateConfig template = langConfig.getTemplateDef().get(templateName);
            HashMap<String, Object> indivModel = new HashMap<String, Object>();
            indivModel.put("Options", template.getOptions());
            dependencyModel.put(templateName, indivModel);

            // Generate code from templates
            String code;
            try {
                CodeByLine codeLines = CodeByLine.fromString(TemplateEngine.render(
                        FileSystem.getResource(template.getTemplateFile()),
                        mergeModels(sharedModel, indivModel)
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
                talkingWindow.error("Template file \"" + template.getTemplateFile().getRelativePath() + "\" not found");
                talkingWindow.unindent();
                continue;
            }

            // Output to model
            if (template.getOutputKey() != null) {
                sharedModel.put(template.getOutputKey(), code);
            }

            // Output to file
            if (template.getOutputFile() != null) {
                String filePath = renderedCodeRoot(config) + "/" +
                        TemplateEngine.render(template.getOutputFile(), currentModel);
                String fileFolder = FileSystem.getParentPath(filePath);
                if (!FileSystem.exists(fileFolder)) {
                    FileSystem.createFolder(fileFolder);
                }

                indivModel.put("GeneratedFileName", new java.io.File(filePath).getName());
                indivModel.put("GeneratedFilePath", FileSystem.getRawFile(filePath).getPath());

                boolean exists = FileSystem.exists(filePath);
                TemplateConfig.OverwriteOptions overwrite = template.getOverwrite();
                if (regen && overwrite == TemplateConfig.OverwriteOptions.SKIP)
                    overwrite = TemplateConfig.OverwriteOptions.BACKUP;
                talkingWindow.show(" -> " + filePath);
                if (exists && overwrite == TemplateConfig.OverwriteOptions.SKIP) {
                    talkingWindow.showLine(" (skipped)");
                    continue;
                }
                if (exists) {
                    if (FileSystem.compareFileToString(filePath, code)) {
                        talkingWindow.show(" (skipped, identical)");
                    } else {
                        if (overwrite == TemplateConfig.OverwriteOptions.FORCE) {
                            talkingWindow.show(" (force overwrite)");
                            FileSystem.writeFile(filePath, code);
                        }
                        else {
                            talkingWindow.show(" (backup and overwrite)");
                            FileSystem.backup(filePath); // Backup the old files
                            FileSystem.writeFile(filePath, code);
                        }
                    }
                }
                else {
                    FileSystem.writeFile(filePath, code);
                }

                if (template.getAfterFileGen() != null) {
                    CommandConfig afterGen = template.getAfterFileGen();
                    String[] commands = new String[afterGen.getArguments().length + 1];
                    commands[0] = afterGen.getExecute();
                    for (int i = 1; i < commands.length; ++i) {
                        commands[i] = TemplateEngine.render(afterGen.getArguments()[i - 1], mergeModels(currentModel, indivModel));
                    }
                    long timeout = 1000L * afterGen.getTimeout();

                    talkingWindow.showLine("");
                    talkingWindow.indent();
                    talkingWindow.showLine("After generation action: ");
                    talkingWindow.indent();
                    talkingWindow.showLine(String.format("(%s)$ %s", fileFolder, StringUtil.join(commands, " ")));
                    talkingWindow.show("Exit status (-1 means exception): " + ExternalSystem.runExternalCommand(FileSystem.getRawFile(fileFolder), timeout, commands));
                    talkingWindow.unindent();
                    talkingWindow.unindent();
                }
            }

            talkingWindow.showLine("");
        }

        talkingWindow.showLine("All set, good luck!");
        talkingWindow.showLine("");
    }

    private HashMap<String, Object> mergeModels(HashMap<String, Object> ... models) {
        HashMap<String, Object> merged = new HashMap<String, Object>();
        for (HashMap<String, Object> model: models)
            merged.putAll(model);
        return merged;
    }

    private boolean checkDependency(TemplateDependencyConfig.Dependency dependency, HashSet<String> hasKeys, HashSet<String> hasTemplates) {
        if (dependency instanceof TemplateDependencyConfig.KeyDependency) {
            return hasKeys.contains(((TemplateDependencyConfig.KeyDependency)dependency).getKey());
        }
        else if (dependency instanceof TemplateDependencyConfig.TemplateDependency) {
            return hasTemplates.contains(((TemplateDependencyConfig.TemplateDependency)dependency).getTemplate());
        }
        else if (dependency instanceof TemplateDependencyConfig.OneOfDependency) {
            TemplateDependencyConfig.OneOfDependency oneOfDep = (TemplateDependencyConfig.OneOfDependency)dependency;
            for (TemplateDependencyConfig.Dependency dep: oneOfDep.getDependencies()) {
                if (checkDependency(dep, hasKeys, hasTemplates)) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalStateException("Invalid types of TemplateDependencyConfig.Dependency");
    }

    public String getSource() {
        GreedConfig config = Utils.getGreedConfig();
        LanguageConfig langConfig = config.getLanguage().get(Language.getName(currentLang));
        String filePath = renderedCodeRoot(config) + "/" +
                TemplateEngine.render(langConfig.getTemplateDef().get(langConfig.getSubmitTemplate()).getOutputFile(), currentModel);

        talkingWindow.showLine("Getting source code from " + filePath);
        talkingWindow.indent();
        String result = "";
        if (!FileSystem.exists(filePath)) {
            talkingWindow.error("Source code file doesn't exist");
        }
        else {
            try {
                CodeByLine code = CodeByLine.fromInputStream(FileSystem.getResource(new ResourcePath(filePath, false)));

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
