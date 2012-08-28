package greed.code;

import greed.model.Param;
import greed.model.Primitive;
import greed.model.Type;
import org.junit.Assert;
import org.junit.Test;

/**
 * Greed is good! Cheers!
 */
public class CStyleLanguageTraitTest {
	@Test
	public void parseStringArrayTest() {
		CStyleLanguageTrait trait = CStyleLanguageTrait.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("{    \n");
		sb.append("\"Abcde\"\n, \"12345\", \n\n\n");
		sb.append("\"Hello\"\n");
		sb.append(", \"world\"    }");
		String[] parsedValueList = trait.parseValue(sb.toString(), new Param("arg", new Type(Primitive.STRING, 1))).getValueList();
		for (String pv: parsedValueList)
			System.out.println(pv);
		Assert.assertArrayEquals("Parsed value is " + parsedValueList, parsedValueList,
				new String[] { "\"Abcde\"", "\"12345\"", "\"Hello\"", "\"world\"" });
	}

	@Test
	public void parseOtherArrayTest() {
		CStyleLanguageTrait trait = CStyleLanguageTrait.getInstance();
		StringBuffer sb = new StringBuffer();
		sb.append("{123LL,    ");
		sb.append("\n124LL\n,125LL,999LL,\n\n12LL\n,123LL\n    } \n");
		String[] parsedValueList = trait.parseValue(sb.toString(), new Param("arg", new Type(Primitive.LONG, 1))).getValueList();
		for (String pv: parsedValueList)
			System.out.println(pv);
		Assert.assertArrayEquals("Parsed value is " + parsedValueList, parsedValueList,
				new String[] { "123LL", "124LL", "125LL", "999LL", "12LL", "123LL" });
	}
}
