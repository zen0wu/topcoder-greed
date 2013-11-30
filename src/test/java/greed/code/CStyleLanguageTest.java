package greed.code;

import greed.code.lang.CppLanguage;
import greed.model.Param;
import greed.model.Type;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 */
public class CStyleLanguageTest {
    @Test
    public void parseStringArrayTest() {
        CppLanguage trait = CppLanguage.instance;
        StringBuilder sb = new StringBuilder();
        sb.append("{    \n");
        sb.append("\"Abcde\"\n, \"12345\", \n\n\n");
        sb.append("\"Hello\"\n");
        sb.append(", \"world\"    }");
        String[] parsedValueList = trait.parseValue(sb.toString(), new Param("arg", Type.STRING_ARRAY_TYPE, 0)).getValueList();
        for (String pv : parsedValueList)
            System.out.println(pv);
        Assert.assertArrayEquals("Parsed value is " + Arrays.toString(parsedValueList), parsedValueList,
                new String[]{"\"Abcde\"", "\"12345\"", "\"Hello\"", "\"world\""});
    }

    @Test
    public void parseOtherArrayTest() {
        CppLanguage trait = CppLanguage.instance;
        StringBuilder sb = new StringBuilder();
        sb.append("{123,    ");
        sb.append("\n123\n,125,999,\n\n12\n,123\n    } \n");
        String[] parsedValueList = trait.parseValue(sb.toString(), new Param("arg", Type.LONG_ARRAY_TYPE, 0)).getValueList();
        for (String pv : parsedValueList)
            System.out.println(pv);
        Assert.assertArrayEquals("Parsed value is " + Arrays.toString(parsedValueList), parsedValueList,
                new String[]{"123LL", "123LL", "125LL", "999LL", "12LL", "123LL"});
    }
}
