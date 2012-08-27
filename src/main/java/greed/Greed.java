package greed;

import com.topcoder.client.contestant.ProblemComponentModel;
import com.topcoder.shared.problem.Renderer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import greed.model.*;
import greed.transform.CodeByLine;
import greed.ui.ConfigurationDialog;
import greed.ui.GreedEditorPanel;
import greed.util.FileSystem;
import greed.template.TemplateEngine;
import greed.util.Configuration;
import greed.util.Log;

import static greed.util.Configuration.Keys;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Greed {
    public static final String APP_NAME = "Greed";

    private Language currentLang;
    private Problem currentProb;
    private Contest currentContest;
    private HashMap<String, Object> currentTemplateModel;

    private GreedEditorPanel talkingWindow;
    private boolean firstUsing;

    public Greed() {
        // Entrance of all program
        Log.i("Create Greed Plugin");
        this.talkingWindow = new GreedEditorPanel();
        this.firstUsing = true;
    }

    /*
     * Cache the editor
     */
    public boolean isCacheable() {
        return false;
    }

    /*
     * Called when open the coding frame
     * Like FileEdit, a log window is used
     */
    public JPanel getEditorPanel() {
        return talkingWindow;
    }

    /*
     * Ignore the given source code
     */
    public void setSource(String source) {}

    public String getSource() {
        String codeDir = Configuration.getString(Keys.CODE_ROOT);
        String relativePath = TemplateEngine.render(Configuration.getString(Keys.PATH_PATTERN), currentTemplateModel);
        String fileName = TemplateEngine.render(Configuration.getString(Keys.FILE_NAME_PATTERN), currentTemplateModel);

        // Create source file if not exists
	    Config langSpecConfig = Configuration.getConfig(Keys.getTemplateKey(currentLang));
        String filePath = codeDir + "/" + relativePath + "/" + fileName + "." +  langSpecConfig.getString(Keys.SUBKEY_EXTENSION);

	    // Get begincut and endcut tag
	    String beginCut = langSpecConfig.getString(Keys.SUBKEY_CUTBEGIN);
	    String endCut = langSpecConfig.getString(Keys.SUBKEY_CUTEND);

        try {
	        CodeByLine code = CodeByLine.fromInputStream(FileSystem.getInputStream(filePath));

            // Cut the code
	        boolean cutting = false;
	        StringBuffer buf = new StringBuffer();
	        for (String line: code.getLines()) {
		        System.err.println(line);
		        if (line.equals(beginCut))
			        cutting = true;
		        else if (line.equals(endCut))
			        cutting = false;
		        else if (!cutting) {
			        buf.append(line);
			        buf.append("\n");
		        }
	        }

            return buf.toString();
        }
        catch (IOException e) {
            talkingWindow.say("Cannot fetch your source code. Please check the logs, and make sure your source code is present");
            talkingWindow.say("Now I'm giving out a empty string!");
            Log.e("Error getting the source", e);
            return "";
        }
    }

    public void startUsing() {
        Log.i("Start using called");
        talkingWindow.clear();
        if (firstUsing) {
            talkingWindow.say(String.format("Hi, this is %s.", APP_NAME));
            talkingWindow.say("Nice to help you with your contest");
        }
        else {
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
        // Check whether workspace is set
        if (Configuration.getWorkspace() == null || "".equals(Configuration.getWorkspace())) {
            talkingWindow.say("It seems that you haven't set your workspace, go set it!");
            Log.e("Workspace not set");
            return;
        }

        currentContest = Convert.convertContest(componentModel);
        currentProb = Convert.convertProblem(componentModel);
        currentLang = Convert.convertLanguage(language);

        talkingWindow.say("Hmmm, it's a problem with " + currentProb.getScore() + " points.");

        try {
            setProblem(currentContest, currentProb, currentLang);
        }
        catch (Throwable e) {
            talkingWindow.say("Oops, something wrong! It says \"" + e.getMessage() + "\"");
            talkingWindow.say("Please see the logs for details.");
            Log.e("Set problem failed", e);
        }
    }

    private void setProblem(Contest contest, Problem problem, Language language) {
        // Create model map
        currentTemplateModel = new HashMap<String, Object>();
        currentTemplateModel.put("Contest", contest);
        currentTemplateModel.put("Problem", problem);

        String relativePath = TemplateEngine.render(Configuration.getString(Keys.PATH_PATTERN), currentTemplateModel);
        String fileName = TemplateEngine.render(Configuration.getString(Keys.FILE_NAME_PATTERN), currentTemplateModel);

        // Create source directory
        String codeDir = Configuration.getString(Keys.CODE_ROOT);
        codeDir += "/" + relativePath;
        if (!FileSystem.exists(codeDir)) {
            talkingWindow.say("I'm creating folder " + codeDir);
            FileSystem.createFolder(codeDir);
        }

        // Create source file if not exists
	    Config langSpecConfig = Configuration.getConfig(Keys.getTemplateKey(language));
        String filePath = codeDir + "/" + fileName + "." + langSpecConfig.getString(Keys.SUBKEY_EXTENSION);
        boolean exists = FileSystem.exists(filePath);
        boolean override = Configuration.getBoolean(Keys.OVERRIDE);
        talkingWindow.say("Source code will be generated, \"" + filePath + "\"" + ", in your workspace");
        if (exists)
            talkingWindow.say("Seems the file has been created");
        if (exists && !override) {
            // Skip old files due to override options
            talkingWindow.say("This time I'll not override it, if you say so.");
        }
        else {
            if (exists) {
                talkingWindow.say("Overriding, old files will be renamed");
                // Backup the old files
                FileSystem.backup(filePath);
            }

            // Create code template
            // First, set the language of template engine
            TemplateEngine.switchLanguage(language);

            // Bind additional model
            currentTemplateModel.put("ClassName", problem.getClassName());
            currentTemplateModel.put("Method", problem.getMethod());
            currentTemplateModel.put("Examples", problem.getTestcases());
            currentTemplateModel.put("NumOfExamples", problem.getTestcases().length);
            boolean useArray = problem.getMethod().getReturnType().isArray();
            currentTemplateModel.put("UsePrintArray", useArray);
            for (Param param: problem.getMethod().getParams()) useArray |= param.getType().isArray();
            currentTemplateModel.put("UseArray", useArray);
            currentTemplateModel.put("RecordRuntime", Configuration.getBoolean(Keys.RECORD_RUNTIME));

            talkingWindow.say("I'm generating source code for you~");
            // Generate test code
            try {
                String tmplPath = langSpecConfig.getString(Keys.SUBKEY_TEST_TEMPLATE_FILE);
                talkingWindow.say("Using test template \"" + tmplPath + "\"");

                InputStream testTmpl = FileSystem.getInputStream(tmplPath);
                String testCode = TemplateEngine.render(testTmpl, currentTemplateModel);
                currentTemplateModel.put("TestCode", testCode);
            }
            catch (FileNotFoundException e) {
                talkingWindow.say("No testing template, no testing code for you.");
                Log.w("Testing template not found, probably because user specify a non-exist testing template, resulting code without testing module");
            }
            catch (ConfigException e) {
                talkingWindow.say("What's that about the testing template? I didn't understand.");
                Log.w("Incorrect test template configuration", e);
            }

            // Generate main code
            String sourceCode;
            try {
	            String tmplPath = langSpecConfig.getString(Keys.SUBKEY_TEMPLATE_FILE);
                talkingWindow.say("Using source template \"" + tmplPath + "\"");

                InputStream codeTmpl = FileSystem.getInputStream(tmplPath);
                sourceCode = TemplateEngine.render(codeTmpl, currentTemplateModel);
            }
            catch (FileNotFoundException e) {
                talkingWindow.say("Oh no, where's your source code template?");
                talkingWindow.say("You have to start with a empty file yourself :<");
                Log.e("Source code template not found, this is fatal error, source code will not be generated");
                return;
            }
            catch (ConfigException e) {
                talkingWindow.say("No configuration for source code template, I'm giving up!");
                Log.e("Incorrect code template configuration", e);
                return;
            }

            // Write to file
            FileSystem.writeFile(filePath, sourceCode);
        }
        talkingWindow.say("All set, good luck!");
    }
}
