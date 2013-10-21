package greed.code.lang;

import greed.code.LanguageRenderer;
import greed.code.LanguageTrait;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

import java.util.ArrayList;

/**
 * An base implementation of {@link LanguageRenderer} and {@link LanguageTrait}.
 * <p>
 * This implementation expects to support traits that are common to almost all
 * languages, so that each language's trait implementation can be benefited by
 * extending this class.
 *
 * @since 2.0
 *
 * @author Jongwook Choi
 * @author Shiva Wu
 */
public abstract class AbstractLanguage implements LanguageTrait, LanguageRenderer {

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
            StringBuilder buf = new StringBuilder();
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
            Param paramWithPrim = new Param(param.getName(), new Type(param.getType().getPrimitive(), 0), param.getIndex());
            for (int i = 0; i < valueList.length; i++) {
            	valueList[i] = renderParamValue(new ParamValue(paramWithPrim, valueList[i].trim()));
            }
            return new ParamValue(param, valueList);
        }
    }

    @Override
    public String renderParamList(Param[] params) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) buf.append(", ");
            buf.append(renderParam(params[i]));
        }
        return buf.toString();
    }
}
