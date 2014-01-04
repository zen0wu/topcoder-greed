package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.model.Language;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * @see HTMLRenderer
 *
 * @author Jongwook Choi
 *
 */
public class HTMLRendererTest {

    private TemplateEngine engine;

    @Before
    public void setup() {
        engine = TemplateEngine.newLanguageEngine(Language.CPP);
    }

    static final ParamValue GRID_LIKE_STRING_ARRAY_PARAM =
            new ParamValue(
                    new Param("gridParam", Type.STRING_ARRAY_TYPE, 0),
                    new String[] { "####", "#..#", "#..#", "####" }
                );


    @Test
    public void testRenderHtmlGridFilter() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("gridParam", GRID_LIKE_STRING_ARRAY_PARAM);
        String result = engine.render("${gridParam;html(grid)}", model);

        System.out.println(result);
        assertThat(result, equalTo("{####,<br />&nbsp;#..#,<br />&nbsp;#..#,<br />&nbsp;####}"));
    }

    @Test
    public void testRenderHtmlNonGridFilter() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("gridParam", GRID_LIKE_STRING_ARRAY_PARAM);
        String result = engine.render("${gridParam;html}", model);

        System.out.println(result);
        assertThat(result, equalTo("{ ####, #..#, #..#, #### }"));
    }

    @Test
    public void testRenderHtmlStringQuote() {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("stringParam", new ParamValue(
                    new Param("stringParam", Type.STRING_TYPE, 0),
                    "\"quoted\"")
                );

        String result = engine.render("${stringParam;html}", model);
        System.out.println(result);
        assertThat(result, equalTo("&quot;quoted&quot;"));

        model.put("gridParam", new ParamValue(
                    new Param("gridParam", Type.STRING_ARRAY_TYPE, 1),
                    new String[] {"\"\"", ".."}
                    )
                );
        String gridResult = engine.render("${gridParam;html(grid)}", model);
        System.out.println(gridResult);
        assertThat(gridResult, equalTo("{&quot;&quot;,<br />&nbsp;..}"));
    }

}
