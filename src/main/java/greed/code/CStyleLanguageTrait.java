package greed.code;

import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;

import java.util.ArrayList;

/**
 * Greed is good! Cheers!
 */
public class CStyleLanguageTrait implements LanguageTrait {
    private static CStyleLanguageTrait ourInstance = new CStyleLanguageTrait();

    public static CStyleLanguageTrait getInstance() {
        return ourInstance;
    }

    private CStyleLanguageTrait() {
    }

    @Override
    public String getCommentPrefix() {
        return "//";
    }

    @Override
    public ParamValue parseValue(String value, Param param) {
        if (!param.getType().isArray())
            return new ParamValue(param, value);

        value = value.trim();
        value = value.substring(1, value.length() - 1);
        value = value.replaceAll("\n", "");
        if (param.getType().getPrimitive() == Primitive.STRING) {
            boolean inString = false;
            ArrayList<String> valueList = new ArrayList<String>();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < value.length(); ++i) {
                char c = value.charAt(i);
                if (c == '"') {
                    if (inString) {
                        valueList.add('"' + buf.toString() + '"');
                    } else {
                        buf.setLength(0);
                    }
                    inString = !inString;
                } else if (inString) {
                    buf.append(c);
                }
            }

            return new ParamValue(param, valueList.toArray(new String[0]));
        } else {
            String[] valueList = value.split(",");
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < valueList.length; i++) {
                if (i > 0) buf.append(", ");
                buf.append(valueList[i].trim());
            }
            return new ParamValue(param, buf.toString());
        }
    }
}
