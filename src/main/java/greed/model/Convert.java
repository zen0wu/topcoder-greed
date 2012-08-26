package greed.model;

import com.topcoder.shared.problem.DataType;
import com.topcoder.shared.problem.TestCase;

public class Convert {
    public static Contest convertContest(com.topcoder.client.contestant.ProblemComponentModel problem) {
        String fullName = problem.getProblem().getRound().getContestName();
        boolean hasDivision = fullName.indexOf("DIV") != -1;
        if (!hasDivision)
            return new Contest(fullName, -1);
        else {
            int sp = fullName.indexOf("DIV");
            String contestName = fullName.substring(0, sp - 1);
            String divNum = fullName.substring(sp + 4);
            return new Contest(contestName, Integer.parseInt(divNum));
        }
    }

    public static Language convertLanguage(com.topcoder.shared.language.Language lang) {
        String langName = lang.getName();
        if ("C++".equals(langName)) return Language.CPP;
        if ("Python".equals(langName)) return Language.PYTHON;
        if ("Java".equals(langName)) return Language.JAVA;
        if ("VB".equals(langName)) return Language.VB;
        if ("C#".equals(langName)) return Language.CSHARP;
        return null;
    }

    public static Problem convertProblem(com.topcoder.client.contestant.ProblemComponentModel problem) {
        Param[] params = new Param[problem.getParamNames().length];
        for (int i = 0; i < params.length; ++i)
            params[i] = new Param(problem.getParamNames()[i], convertType(problem.getParamTypes()[i]));
        Method method = new Method(problem.getMethodName(), convertType(problem.getReturnType()), params);

        Testcase[] cases = new Testcase[problem.getTestCases().length];
        for (int i = 0; i < cases.length; ++i) {
            TestCase tc = problem.getTestCases()[i];
            ParamValue[] input = new ParamValue[tc.getInput().length];
            for (int j = 0; j < input.length; j++)
                input[j] = new ParamValue(params[j], tc.getInput()[j]);
            ParamValue output = new ParamValue(new Param("expected", method.getReturnType()), tc.getOutput());
            cases[i] = new Testcase(i, input, output);
        }

        return new Problem(
                problem.getProblem().getName(),
                problem.getPoints().intValue(),
                problem.getClassName(),
                method,
                cases
        );
    }

    public static Type convertType(DataType dt) {
        String typeName = dt.getBaseName();
        Primitive type = null;
        if ("int".equals(typeName)) type = Primitive.INT;
        if ("String".equals(typeName)) type = Primitive.STRING;
        if ("long".equals(typeName)) type = Primitive.LONG;
        if ("double".equals(typeName)) type = Primitive.DOUBLE;
        if ("bool".equals(typeName)) type = Primitive.BOOL;
        return new Type(type, dt.getDimension());
    }
}
