package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.conf.ConfigException;
import greed.model.Language;
import greed.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JavaTemplateTest {

    private InputStream codeTemplate;
    private InputStream testTemplate;
    private InputStream testJunitTemplate;

    Map<String, Object> model = TestModelFixtures.buildStubbingModel();
    TemplateEngine engine;

    @BeforeClass
    public static void initializeGreed() throws ConfigException {
        // TODO : Why at all do we need this?
        Utils.initialize();
    }

    @Before
    public void setupTemplates() throws IOException {
        this.codeTemplate = getClass().getResourceAsStream("/templates/source/java.tmpl");
        assertThat(this.codeTemplate, notNullValue());

        this.testTemplate = getClass().getResourceAsStream("/templates/test/java.tmpl");
        assertThat(this.testTemplate, notNullValue());

        this.testJunitTemplate = getClass().getResourceAsStream("/templates/unittest/junit.java.tmpl");
        assertThat(this.testTemplate, notNullValue());

        engine = TemplateEngine.newLanguageEngine(Language.JAVA);
    }

    @Test
    public void renderJavaCode() {
        String test = engine.render(testTemplate, model);
        model.put("TestCode", test);
        String code = engine.render(codeTemplate, model);
        System.out.println(code);

        // TODO verify to make test fail on malfunctioning
    }

    @Test
    public void renderJavaCode_junit() {
        String code = engine.render(testJunitTemplate, model);
        System.out.println(code);

        // TODO verify to make test fail on malfunctioning
    }
}
