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
}
