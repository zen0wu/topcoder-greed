package greed.template;

import static org.junit.Assert.*;
import greed.model.Language;
import greed.model.Contest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 *
 * @see ContestCategoryRenderer
 *
 * @author Vexorian
 * @author Jongwook Choi
 */
public class ContestCategoryRendererTest {

    private Map<String, Object> createModel(String key, String name, int div) {
        return Collections.singletonMap(key, (Object) new Contest(name, div ) );
    }

    @Before
    public void setup() {
        TemplateEngine.switchLanguage(Language.CPP);
    }

    @Test
    public void testContestCategory() {
        String TEMPLATE_SRC = "${Contest;category}";

        Map<String, String> cases = new HashMap<String, String>();
        cases.put("Test SRM Beta 2", "Other");
        cases.put("Single Round Match 245", "SRM");
        cases.put("single round match 250", "SRM");
        cases.put("SRM 333.5", "SRM");
        cases.put("SRM 1", "SRM");
        cases.put("SRM 100 Div 1", "SRM");
        cases.put("TCO 06 Round 1", "TCO");
        cases.put("TopCoder Open 06 Round 1", "TCO");
        cases.put("TCO 06 Semifinal", "TCO");
        cases.put("TCO 13 Qual 2", "TCO");
        cases.put("TCHS 123", "TCHS");
        cases.put("TCCC 2004 Round 1", "TCCC");

        for(Map.Entry<String, String> entry : cases.entrySet()) {
            Map<String, Object> model = createModel("Contest", entry.getKey(), 1);
            assertEquals(entry.getValue(), TemplateEngine.render(TEMPLATE_SRC, model));
        }
    }

    @Test
    public void testContestCategorySRM() {
        String TEMPLATE_SRC = "${Contest;category(srm=25)}";

        Map<String, String> cases = new HashMap<String, String>();
        cases.put("Single Round Match 245", "SRM 225-249");
        cases.put("single round match 250", "SRM 250-274");
        cases.put("SRM 333.5", "SRM 325-349");
        cases.put("SRM 1", "SRM 0-24");
        cases.put("SRM 100 Div 1", "SRM 100-124");

        for(Map.Entry<String, String> entry : cases.entrySet()) {
            Map<String, Object> model = createModel("Contest", entry.getKey(), 1);
            assertEquals(entry.getValue(), TemplateEngine.render(TEMPLATE_SRC, model));
        }
    }

    
    @Test
    public void testContestCategoryAdvanced() {
        assertEquals("SRM 0-99", TemplateEngine.render(
            "${Contest;category(srm=100)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals("SRM 75-99", TemplateEngine.render(
            "${Contest;category(srm=)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals("SRM 77", TemplateEngine.render(
            "${Contest;category(srm=1)}",
            createModel("Contest", "SRM 77", 1))
        );

    }

}