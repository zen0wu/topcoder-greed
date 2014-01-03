package greed.code.lang;

import greed.code.LanguageRenderer;
import greed.code.LanguageTrait;
import greed.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

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
        value = value.trim(); //need a second trim in case it is an empty list {  }
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
        } else if (value.length() == 0) {
            //Empty array
            return new ParamValue( param, new String[]{} );
        } else {
            String[] valueList = value.split(",");
            Param paramWithPrim = new Param(param.getName(), Type.primitiveType(param.getType().getPrimitive()), param.getIndex());
            for (int i = 0; i < valueList.length; i++) {
            	valueList[i] = renderParamValue(new ParamValue(paramWithPrim, valueList[i].trim()));
            }
            return new ParamValue(param, valueList);
        }
    }

    @Override
    public String renderMethod(Method method) {
        return renderType(method.getReturnType()) + " " + method.getName() + "(" + renderParamList(method.getParams()) + ")";
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

    @Override
    public List<NamedRenderer> getOtherRenderers() {
        ArrayList<NamedRenderer> namedRenderers = new ArrayList<NamedRenderer>();
        namedRenderers.add(new NamedRenderer() {
            @Override
            public String render(Object o, String s, Locale locale) {
                if (o instanceof Type)
                    return renderZeroValue((Type) o);
                return "";
            }

            @Override
            public String getName() {
                return "zeroval";
            }

            @Override
            public RenderFormatInfo getFormatInfo() {
                return null;
            }

            @Override
            public Class<?>[] getSupportedClasses() {
                return new Class<?>[]{Type.class};
            }
        });
        return namedRenderers;
    }
}
