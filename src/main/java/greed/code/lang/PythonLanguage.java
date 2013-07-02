package greed.code.lang;

import greed.code.LanguageRenderer;
import greed.code.LanguageTrait;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

/**
 * Support for the python language.
 *
 * @since 1.5
 * @author Jongwook Choi (wook)
 *
 */
public class PythonLanguage extends CStyleLanguage implements LanguageRenderer, LanguageTrait {

    /*
     * TODO : This class extends {@link CStyleLanguage},
     * which seems to be awkward since python is not a C-style language!
     * But I've done this just because lots of code bases should be copied
     * from previously existing CStyleLanguage (e.g. {@link #parseValue}).
     * After an appropriate refactoring, it will be fixed.
     */

    public static final PythonLanguage instance = new PythonLanguage();

    protected PythonLanguage() {
    }

    @Override
    public String getCommentPrefix() {
        return "#";
    }

    @Override
    public String renderPrimitive(Primitive primitive) {
        // python has no type...
        return "";
    }

    @Override
    public String renderType(Type type) {
        // python has no type...
        return "";
    }

    @Override
    public String renderParam(Param param) {
        return param.getName();
    }

    @Override
    public String renderParamValue(ParamValue paramValue) {
        String value = paramValue.getValue();

        // yep, just return as-is
        return value;
    }

    @Override
    public String renderParamList(Param[] params) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) buf.append(", ");
            buf.append(renderParam(params[i]));
        }
        return buf.toString();
    }

    @Override
    public String renderZeroValue(Type type) {
        if (type.isArray()) {
            return "()"; 	// empty tuple.
        }
        else {
            switch (type.getPrimitive()) {
            case BOOL: return "False";
            case STRING: return "\"\"";
            case INT:
            case LONG:
                return "0";
            case DOUBLE:
                return "0.0";
            }
        }
        return "";
    }

}
