package greed.code;

import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

import java.util.ArrayList;

/**
 * Greed is good! Cheers!
 */
public abstract class CStyleLanguage implements LanguageTrait, LanguageRenderer {
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
            // TODO: escape \" in string
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
            Param paramWithPrim = new Param(param.getName(), new Type(param.getType().getPrimitive(), 0));
            for (int i = 0; i < valueList.length; i++) {
                if (i > 0) buf.append(", ");
                buf.append(renderParamValue(new ParamValue(paramWithPrim, valueList[i].trim())));
            }
            return new ParamValue(param, buf.toString());
        }
    }
}
