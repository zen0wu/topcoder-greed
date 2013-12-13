package greed.template;

import static org.junit.Assert.*;
import greed.model.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 *
 * @see StringUtilRenderer
 *
 * @author Shiva Wu
 * @author Jongwook Choi
 */
public class StringUtilRendererTest {

    private Map<String, Object> createModel(String key, String value) {
        return Collections.singletonMap(key, (Object) value);
    }

    @Before
    public void setup() {
        TemplateEngine.switchLanguage(Language.CPP);
    }

    @Test
    public void testContestName() {
        Map<String, Object> model = createModel("ContestName", "SRM    245");
        String result = TemplateEngine.render("${ContestName;string(lower,removespace)}", model);
        Assert.assertEquals(result, "srm245");
    }

    @Test
    public void testAbbr() {
        Map<String, Object> model;

        model = createModel("ContestName", "Single Round Match    245");
        assertEquals("SRM245", TemplateEngine.render("${ContestName;string(abbr)}", model));

        model = createModel("ContestName", "TCHS 21 Div 1");
        assertEquals("TCHS21D1", TemplateEngine.render("${ContestName;string(abbr)}", model));

        model = createModel("ContestName", "TCO11");
        assertEquals("TCO11", TemplateEngine.render("${ContestName;string(abbr)}", model));
    }

    @Test
    public void testUpFirst() {
        Map<String, Object> model = createModel("Var", "topcoder");
        Assert.assertEquals("Topcoder", TemplateEngine.render("${Var;string(upfirst)}", model));
    }

    @Test
    public void testRemovespace() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("V1", "A B C");
        model.put("V2", "   Topcoder");
        model.put("V3", "   Single Round Match     ");

        Assert.assertEquals("ABC", TemplateEngine.render("${V1;string(removespace)}", model));
        Assert.assertEquals("Topcoder", TemplateEngine.render("${V2;string(removespace)}", model));
        Assert.assertEquals("SingleRoundMatch", TemplateEngine.render("${V3;string(removespace)}", model));
    }

    @Test
    public void testUnquote() {
        Map<String, Object> model;

        model = createModel("Var", "\"\"");
        Assert.assertEquals("", TemplateEngine.render("${Var;string(unquote)}", model));

        model = createModel("Var", "\"####\"");
        Assert.assertEquals("####", TemplateEngine.render("${Var;string(unquote)}", model));
    }

    @Test
    public void testFilterChains() {
        Map<String, Object> model ;

        //model = createModel("Var", "Topcoder Single Round Match 200");
        //Assert.assertEquals("Srm 200-224",
        //        TemplateEngine.render("${Var;string(contestcategory, lower, upFirst, unquote)}", model));

        model = createModel("Var", "Topcoder Single Round Match 200");
        Assert.assertEquals("Topcoder Single Round Match 200",
                TemplateEngine.render("${Var;string()}", model));
    }

}
