package greed;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import greed.code.CodeByLine;
import greed.code.LanguageManager;
import greed.code.transform.AppendingTransformer;
import greed.code.transform.BlockCleaner;
import greed.code.transform.BlockRemover;
import greed.code.transform.ContinuousBlankLineRemover;
import greed.model.*;
import greed.template.TemplateEngine;
import greed.ui.ConfigurationDialog;
import greed.ui.GreedEditorPanel;
import greed.util.Configuration;
import greed.util.Debug;
import greed.util.FileSystem;
import greed.util.Log;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static greed.util.Configuration.Keys;

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
        Log.i("Create Greed Plugin");
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

    public String getSource() {
        String codeDir = Configuration.getString(Keys.CODE_ROOT);
        String relativePath = TemplateEngine.render(Configuration.getString(Keys.PATH_PATTERN), currentTemplateModel);
        String fileName = TemplateEngine.render(Configuration.getString(Keys.FILE_NAME_PATTERN), currentTemplateModel);

        // Create source file if not exists
        Config langSpecConfig = Configuration.getLanguageConfig(currentLang);
        String filePath = codeDir + "/" + relativePath + "/" + fileName + "." + langSpecConfig.getString(Keys.SUBKEY_EXTENSION);

        try {
            CodeByLine code = CodeByLine.fromInputStream(FileSystem.getInputStream(filePath));

            if (LanguageManager.getInstance().getPostTransformer(currentLang) != null)
                code = LanguageManager.getInstance().getPostTransformer(currentLang).transform(code);

            code = postprocessCode(code);

            return code.toString();
        } catch (IOException e) {
            talkingWindow.say("Errr... Cannot fetch your source code. Please check the logs, and make sure your source code is present");
            talkingWindow.say("Now I'm giving out a empty string!");
            Log.e("Error getting the source", e);
            return "";
        }
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

        talkingWindow.say("Hmmm, it's a problem with " + currentProb.getScore() + " points. Good choice!");

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
        Config langSpecConfig = Configuration.getLanguageConfig(language);
        TemplateEngine.switchLanguage(language);

        // Create model map
        currentTemplateModel = new HashMap<String, Object>();
        currentTemplateModel.put("Contest", contest);
        currentTemplateModel.put("Problem", problem);

        // Create source directory
        String codeDir;
        {
            String codeRoot = Configuration.getString(Keys.CODE_ROOT);
            String relativePath = TemplateEngine.render(Configuration.getString(Keys.PATH_PATTERN), currentTemplateModel);
            codeDir = codeRoot + "/" + relativePath;

            if (!FileSystem.exists(codeDir)) {
                talkingWindow.say("I'm creating folder " + codeDir);
                FileSystem.createFolder(codeDir);
            }
        }

        String sourceFilePath;
        {
            String fileName = TemplateEngine.render(Configuration.getString(Keys.FILE_NAME_PATTERN), currentTemplateModel);
            sourceFilePath = codeDir + "/" + fileName + "." + langSpecConfig.getString(Keys.SUBKEY_EXTENSION);
        }
        boolean sourceFileExists = FileSystem.exists(sourceFilePath);

        boolean doUnitTest = Configuration.getBoolean(Keys.UNIT_TEST);
        String unitTestFilePath;
        {
            String unitTestFileName = TemplateEngine.render(Configuration.getString(Keys.UNIT_TEST_FILE_NAME_PATTERN), currentTemplateModel);
            unitTestFilePath = codeDir + "/" + unitTestFileName + "." + langSpecConfig.getString(Keys.SUBKEY_EXTENSION);
        }
        boolean unitTestFileExists = FileSystem.exists(unitTestFilePath);

        // If exists and not overriding, return
        talkingWindow.say("Source code will be generated, \"" + sourceFilePath + "\"" + ", in your workspace");
        boolean override = Configuration.getBoolean(Keys.OVERRIDE) || forceOverride;
        if (sourceFileExists && !override) {
            // Skip old files due to override options
            talkingWindow.say("You told me not to override");
            return;
        }
        talkingWindow.say("I'm generating source code for you~");

        {
            // Bind problem template model
            currentTemplateModel.put("ClassName", problem.getClassName());
            currentTemplateModel.put("Method", problem.getMethod());
            currentTemplateModel.put("Examples", problem.getTestcases());
            currentTemplateModel.put("NumOfExamples", problem.getTestcases().length);
            boolean useArray = problem.getMethod().getReturnType().isArray();
            currentTemplateModel.put("ReturnsArray", useArray);
            for (Param param : problem.getMethod().getParams()) useArray |= param.getType().isArray();
            currentTemplateModel.put("HasArray", useArray);
            currentTemplateModel.put("RecordRuntime", Configuration.getBoolean(Keys.RECORD_RUNTIME));
            currentTemplateModel.put("RecordScore", Configuration.getBoolean(Keys.RECORD_SCORE));
            currentTemplateModel.put("CreateTime", System.currentTimeMillis() / 1000);
            currentTemplateModel.put("CutBegin", langSpecConfig.getString(Keys.SUBKEY_CUTBEGIN));
            currentTemplateModel.put("CutEnd", langSpecConfig.getString(Keys.SUBKEY_CUTEND));
        }

        // Generate unit test code
        String unitTestCode = null;
        if (doUnitTest) {
            unitTestCode = generateUnitTestCode(unitTestFilePath, langSpecConfig);
            if (unitTestCode == null)
                talkingWindow.say("I tried, but failed. Fallback to normal test code.");
        }
        // Generate test code, if unit test generation is disabled, or failed
        if (unitTestCode == null) {
            String testCode = generateTestCode(langSpecConfig);
            if (testCode != null)
                currentTemplateModel.put("TestCode", testCode);
        }
        // Generate main source code
        String sourceCode = generateSourceCode(langSpecConfig);

        // Write to file
        writeFileWithBackup(sourceFilePath, sourceCode, sourceFileExists);
        writeFileWithBackup(unitTestFilePath, unitTestCode, unitTestFileExists);

        talkingWindow.say("All set, good luck!");
        talkingWindow.say("");
    }

    private String generateUnitTestCode(String unitTestFilePath, Config langSpecConfig) {
        String sourceCode;
        try {
            String tmplPath = langSpecConfig.getString(Keys.SUBKEY_UNIT_TEST_TEMPLATE_FILE);
            talkingWindow.say("Using unit test template \"" + tmplPath + "\"");

            return generateCodeByTmpl(tmplPath, currentTemplateModel);
        } catch (FileNotFoundException e) {
            talkingWindow.say("No unit test template, no unit test.");
            Log.w("Unit test template not found, probably because user specify a non-exist testing template, resulting code without testing module");
        } catch (ConfigException e) {
            talkingWindow.say("What's that about the unit test template? I didn't understand.");
            Log.w("Incorrect unit test template configuration", e);
        }
        return null;
    }

    private String generateTestCode(Config langSpecConfig) {
        try {
            String tmplPath = langSpecConfig.getString(Keys.SUBKEY_TEST_TEMPLATE_FILE);
            talkingWindow.say("Using test template \"" + tmplPath + "\"");

            return generateCodeByTmpl(tmplPath, currentTemplateModel);
        } catch (FileNotFoundException e) {
            talkingWindow.say("No testing template, no testing code for you.");
            Log.w("Testing template not found, probably because user specify a non-exist testing template, resulting code without testing module");
        } catch (ConfigException e) {
            talkingWindow.say("What's that about the testing template? I didn't understand.");
            Log.w("Incorrect test template configuration", e);
        }
        return null;
    }

    private String generateSourceCode(Config langSpecConfig) {
        // Generate main code
        String sourceCode;
        try {
            String tmplPath = langSpecConfig.getString(Keys.SUBKEY_TEMPLATE_FILE);
            talkingWindow.say("Using source template \"" + tmplPath + "\"");

            return generateCodeByTmpl(tmplPath, currentTemplateModel);
        } catch (FileNotFoundException e) {
            talkingWindow.say("Oh no, where's your source code template?");
            talkingWindow.say("You have to start with a empty file yourself :<");
            Log.e("Source code template not found, this is fatal error, source code will not be generated");
        } catch (ConfigException e) {
            talkingWindow.say("Incorrect configuration for source code template, I'm giving up!");
            Log.e("Incorrect code template configuration", e);
        }
        return null;
    }

    private String generateCodeByTmpl(String tmplPath, HashMap<String, Object> model) throws FileNotFoundException {
        InputStream codeTmpl = FileSystem.getInputStream(tmplPath);
        CodeByLine code = CodeByLine.fromString(TemplateEngine.render(codeTmpl, model));
        code = preprocessCode(code);
        return code.toString();
    }

    private void writeFileWithBackup(String path, String content, boolean exists) {
        if (content != null) {
            if (exists) {
                talkingWindow.say("Overriding \"" + path + "\", old files will be renamed");
                if (FileSystem.getSize(path) == content.length()) {
                    talkingWindow.say("Seems the current file is the same as the code to write, skip!");
                } else
                    FileSystem.backup(path); // Backup the old files
            }

            FileSystem.writeFile(path, content);
        }
    }

    private CodeByLine preprocessCode(CodeByLine input) {
        // Get begincut and endcut tag
        Config langSpecConfig = Configuration.getLanguageConfig(currentLang);
        String beginCut = langSpecConfig.getString(Keys.SUBKEY_CUTBEGIN);
        String endCut = langSpecConfig.getString(Keys.SUBKEY_CUTEND);

        input = new ContinuousBlankLineRemover().transform(input);
        return new BlockCleaner(beginCut, endCut).transform(input);
    }

    private CodeByLine postprocessCode(CodeByLine input) {
        // Get begincut and endcut tag
        Config langSpecConfig = Configuration.getLanguageConfig(currentLang);
        String beginCut = langSpecConfig.getString(Keys.SUBKEY_CUTBEGIN);
        String endCut = langSpecConfig.getString(Keys.SUBKEY_CUTEND);

        input = new BlockRemover(beginCut, endCut).transform(input);
        return new AppendingTransformer(getSignature()).transform(input);
    }
}
