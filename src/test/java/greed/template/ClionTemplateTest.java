package greed.template;

import greed.conf.ConfigException;
import greed.model.Language;
import greed.util.Utils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ClionTemplateTest {

    private InputStream cmakeTemplate;

    Map<String, Object> model = new HashMap<String, Object>();
    TemplateEngine engine;

    @BeforeClass
    public static void initializeGreed() throws ConfigException {
        // TODO : Why at all do we need this?
        Utils.initialize();
    }

    @Before
    public void setupTemplates() throws IOException {
        this.cmakeTemplate = getClass().getResourceAsStream("/templates/clion/CMakeLists.tmpl");
        assertThat(this.cmakeTemplate, notNullValue());

        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    @Test
    public void renderCppCode() {
        model.put("GeneratedFilePath", "GeneratedFilePath");
        model.put("GeneratedFileName", ".GeneratedFileName");
        String cmake = engine.render(cmakeTemplate, model);
        System.out.println(cmake);
    }
}
