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
    public void testContestCategory() {
        String TEMPLATE_SRC = "${ContestName;string(contestcategory)}";

        Map<String, String> cases = new HashMap<String, String>();
        cases.put("Test SRM Beta 2", "Other");
        cases.put("Single Round Match 245", "SRM 225-249");
        cases.put("single round match 250", "SRM 250-274");
        cases.put("SRM 333.5", "SRM 325-349");
        cases.put("SRM 1", "SRM 0-24");
        cases.put("SRM 100 Div 1", "SRM 100-124");
        cases.put("TCO 06 Round 1", "TCO");
        cases.put("TopCoder Open 06 Round 1", "TCO");
        cases.put("TCO 06 Semifinal", "TCO");
        cases.put("TCO 13 Qual 2", "TCO");
        cases.put("TCHS 123", "TCHS");
        cases.put("TCCC 2004 Round 1", "TCCC");

        for(Map.Entry<String, String> entry : cases.entrySet()) {
            Map<String, Object> model = createModel("ContestName", entry.getKey());
            assertEquals(entry.getValue(), TemplateEngine.render(TEMPLATE_SRC, model));
        }
    }

    @Test
    public void testContestCategoryAdvanced() {
        assertEquals("SRM 0-99", TemplateEngine.render(
            "${ContestName;string(contestcategory100)}",
            createModel("ContestName", "SRM 77"))
        );
        assertEquals("SRM 75-99", TemplateEngine.render(
            "${ContestName;string(contestcategoryasdf)}",
            createModel("ContestName", "SRM 77"))
        );
    }

    @Test
    public void testFilterChains() {
        Map<String, Object> model ;

        model = createModel("Var", "Topcoder Single Round Match 200");
        Assert.assertEquals("Srm 200-224",
                TemplateEngine.render("${Var;string(contestcategory, lower, upFirst, unquote)}", model));

        model = createModel("Var", "Topcoder Single Round Match 200");
        Assert.assertEquals("Topcoder Single Round Match 200",
                TemplateEngine.render("${Var;string()}", model));
    }

}
