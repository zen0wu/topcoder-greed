package greed.template;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import greed.model.Language;
import greed.model.Method;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Problem;
import greed.model.Testcase;
import greed.model.Type;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;



/**
 * @see greed.code.lang.PythonLanguage
 *
 * @since 1.5
 * @author Jongwook Choi (wook)
 *
 */
public class PythonTemplateTest {

    InputStream codeTemplate;
    InputStream testTemplate;

    Map<String, Object> model;

    @Before
    public void setupTemplates() throws IOException {
        this.codeTemplate = getClass().getResourceAsStream("/Resource/Template.py");
        assertThat(this.codeTemplate, notNullValue());

        this.testTemplate = getClass().getResourceAsStream("/Resource/Test.py");
        assertThat(this.testTemplate, notNullValue());

        TemplateEngine.switchLanguage(Language.PYTHON);
    }

    @Before
    public void buildModel() {
        this.model = new HashMap<String, Object>();

        // TODO : just copied from CppTemplateTest...
        // we need to refactor out these boilerplates, maybe ModelBuilder?
        Type int0 = new Type(Primitive.INT, 0);
        Type int1 = new Type(Primitive.INT, 1);
        Type long0 = new Type(Primitive.LONG, 0);
        Type long1 = new Type(Primitive.LONG, 1);
        Type str0 = new Type(Primitive.STRING, 0);
        Type str1 = new Type(Primitive.STRING, 1);
        Type double0 = new Type(Primitive.DOUBLE, 0);
        Type double1 = new Type(Primitive.DOUBLE, 1);

        Param param1 = new Param("arg1", int0);
        Param param2 = new Param("arg2", long1);
        Param param3 = new Param("arg3", str1);

        Type retType = str1;    // string list
        Method method = new Method("TestMethod", retType, new Param[] { param1, param2, param3} );

        String[] valueList0 = new String[] { "919, 111, 234", "234, 567, 555" };
        String[] valueList1 = new String[] { "\"a\", \"b\", \"c\"", "\"d\"" };
        String[] valueList2 = new String[] { "\"abcd\", \"efg\"", "\"123\", \"456\"" };
        Testcase case0 = new Testcase(0, new ParamValue[] {
                new ParamValue(param1, "15"),
                new ParamValue(param2, valueList0),
                new ParamValue(param3, valueList1)
            }, new ParamValue(new Param("return", retType), valueList2));

        Problem problem = new Problem("Test", 250, "TestClass", method, new Testcase[]{case0});

        model.put("Problem", problem);
        model.put("ClassName", "Test");
        model.put("Method", problem.getMethod());
        model.put("Examples", problem.getTestcases());
        model.put("NumOfExamples", problem.getTestcases().length);
        model.put("HasArray", true);
        model.put("ReturnsArray", true);
        model.put("RecordRuntime", true);
        model.put("RecordScore", true);
        model.put("CreateTime", System.currentTimeMillis() / 1000);
    }

    @Test
    public void renderPythonTestCode() {
        String testCode = TemplateEngine.render(testTemplate, model);
        System.out.println(testCode);

        // naive and minimal assertions by string matching
        assertThat(testCode, containsString("do_test(arg1, arg2, arg3, __expected"));
        assertThat(testCode, containsString("arg1 = 15"));
        assertThat(testCode, containsString("arg2 = (\n" +
            "            919, 111, 234,\n" +
            "            234, 567, 555\n" +
            "        )"
        ));
        assertThat(testCode, containsString("__expected = (\n" +
            "            \"abcd\", \"efg\",\n" +
            "            \"123\", \"456\"\n" +
            "        )"
        ));
        assertThat(testCode, containsString("return do_test(arg1, arg2, arg3, __expected,"));
    }

    @Test
    public void renderPythonMainCode() {
        String code = TemplateEngine.render(codeTemplate, model);
        System.out.println(code);

        // naive assertions by string matching
        assertThat(code, containsString("class Test:"));
        assertThat(code, containsString("def TestMethod(self, arg1, arg2, arg3):"));
    }
}
