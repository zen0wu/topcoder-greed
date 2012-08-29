package greed.template;

import greed.model.*;

import java.io.FileInputStream;
import java.util.HashMap;

public class JavaTemplateTest {
    public static void main(String[] args) throws Exception {
        TemplateEngine.switchLanguage(Language.JAVA);

	    HashMap<String, Object> model = new CppTemplateTest().buildModel();

	    FileInputStream tmplFile = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/Resource/Template.java");
        FileInputStream testTmplFile = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/Resource/GreedTest.java");
        String test = TemplateEngine.render(testTmplFile, model);
        model.put("TestCode", test);
        String code = TemplateEngine.render(tmplFile, model);
        System.out.println(code);
    }
}
