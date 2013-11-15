package greed.template;

import greed.model.Language;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Greed is good! Cheers!
 */
public class StringUtilRendererTest {
    @Test
    public void test1() {
        TemplateEngine.switchLanguage(Language.CPP);
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("ContestName", "SRM    245");
        String result = TemplateEngine.render("${ContestName;string(lower,removespace)}", model);
        Assert.assertEquals(result, "srm245");
    }

    @Test
    public void test2() {
        TemplateEngine.switchLanguage(Language.CPP);
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("ContestName", "Single Round Match    245");
        String result = TemplateEngine.render("${ContestName;string(abbr)}", model);
        Assert.assertEquals(result, "SRM245");

        model.put("ContestName", "TCHS 21 Div 1");
        Assert.assertEquals("tchs21d1", TemplateEngine.render("${ContestName;string(abbr,lower)}", model));

        model.put("ContestName", "TCO11");
        Assert.assertEquals("tco11", TemplateEngine.render("${ContestName;string(abbr,lower)}", model));
    }
}
