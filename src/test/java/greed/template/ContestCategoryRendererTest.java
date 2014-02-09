package greed.template;

import static org.junit.Assert.*;
import greed.model.Contest;
import greed.model.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        return Collections.singletonMap(key, (Object) new Contest(name, div) );
    }

    private TemplateEngine engine;

    @Before
    public void setup() {
        engine = TemplateEngine.newLanguageEngine(Language.CPP);
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
            assertEquals(entry.getValue(), engine.render(TEMPLATE_SRC, model));
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
            assertEquals(entry.getValue(), engine.render(TEMPLATE_SRC, model));
        }
    }

    @Test
    public void testContestCategoryText() {
        String TEMPLATE_SRC = "${Contest;category(other-text=Otros)}";

        Map<String, String> cases = new HashMap<String, String>();
        cases.put("Test SRM Beta 2", "Otros");
        cases.put("Single Round Match 245", "SRM");
        cases.put("TCO 06 Round 1", "TCO");
        cases.put("TCHS 123", "TCHS");
        cases.put("TCCC 2004 Round 1", "TCCC");

        for(Map.Entry<String, String> entry : cases.entrySet()) {
            Map<String, Object> model = createModel("Contest", entry.getKey(), 1);
            assertEquals(entry.getValue(), engine.render(TEMPLATE_SRC, model));
        }

        TEMPLATE_SRC = "${Contest;category(tco-text=TopCoder Open,tchs-text=Highschool)}";

        cases = new HashMap<String, String>();
        cases.put("Test SRM Beta 2", "Other");
        cases.put("Single Round Match 245", "SRM");
        cases.put("TCO 06 Round 1", "TopCoder Open");
        cases.put("TCHS 123", "Highschool");
        cases.put("TCCC 2004 Round 1", "TCCC");

        for(Map.Entry<String, String> entry : cases.entrySet()) {
            Map<String, Object> model = createModel("Contest", entry.getKey(), 1);
            assertEquals(entry.getValue(), engine.render(TEMPLATE_SRC, model));
        }

    }

    @Test
    public void testContestCategoryAdvanced() {
        assertEquals("SRM 0-99", engine.render(
            "${Contest;category(srm=100)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals("SRM 75-99", engine.render(
            "${Contest;category(srm=)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals("Single Round Match 77", engine.render(
            "${Contest;category(srm=1,srm-text=Single Round Match)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals(" 0-99", engine.render(
            "${Contest;category(srm=100,srm-text=)}",
            createModel("Contest", "SRM 77", 1))
        );
        assertEquals("499", engine.render(
            "${Contest;category(srm=1,no-space,srm-text=)}",
            createModel("Contest", "SRM 499", 1))
        );
        assertEquals("SRM 600", engine.render(
            "${Contest;category(srm=1)}",
            createModel("Contest", "SRM 600", 1))
        );
        assertEquals("SRM 600.5", engine.render(
            "${Contest;category(srm=1)}",
            createModel("Contest", "SRM 600.5", 1))
        );
        assertEquals("SRM 600-601", engine.render(
            "${Contest;category(srm=2)}",
            createModel("Contest", "SRM 600.5 Div 1", 1))
        );
    }

}