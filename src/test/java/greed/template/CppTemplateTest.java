package greed.template;

import greed.model.*;

import java.io.FileInputStream;
import java.util.HashMap;

public class CppTemplateTest {
    public static void main(String[] args) throws Exception {

        Type int0 = new Type(Primitive.INT, 0);
        Type int1 = new Type(Primitive.INT, 1);
        Type long0 = new Type(Primitive.LONG, 0);
        Type long1 = new Type(Primitive.LONG, 1);
        Type str0 = new Type(Primitive.STRING, 0);
        Type str1 = new Type(Primitive.STRING, 1);

        Param param1 = new Param("arg1", int0);
        Param param2 = new Param("arg2", long1);
        Param param3 = new Param("arg3", str1);

        Type retType = str1;

        Method method = new Method("TestMethod", retType, new Param[] {param1, param2, param3});

	    String[] valueList0 = new String[] { "919, 111, 234", "234, 567, 555" };
	    String[] valueList1 = new String[] { "\"a\", \"b\", \"c\"", "\"d\"" };
	    String[] valueList2 = new String[] { "\"abcd\", \"efg\"", "\"123\", \"456\"" };
	    Testcase case0 = new Testcase(0, new ParamValue[]{
                new ParamValue(param1, "15"),
                new ParamValue(param2, valueList0),
                new ParamValue(param3, valueList1)
        }, new ParamValue(new Param("return", retType), valueList2));

        Problem problem = new Problem("Test", 250, "TestClass", method, new Testcase[] { case0 });

        TemplateEngine.switchLanguage(Language.CPP);

        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("Problem", problem);
        model.put("ClassName", "Test");
        model.put("Method", problem.getMethod());
        model.put("Examples", problem.getTestcases());
        model.put("NumOfExamples", problem.getTestcases().length);
        model.put("UseArray", true);
        model.put("UsePrintArray", true);
        model.put("RecordRuntime", true);

        FileInputStream tmplFile = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/Resource/Template.cpp");
        FileInputStream testTmplFile = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/Resource/GreedTest.cpp");
        String test = TemplateEngine.render(testTmplFile, model);
        model.put("TestCode", test);
        String code = TemplateEngine.render(tmplFile, model);
        System.out.println(code);
    }
}
