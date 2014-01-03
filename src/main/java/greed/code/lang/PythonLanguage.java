package greed.code.lang;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import greed.code.LanguageRenderer;
import greed.code.LanguageTrait;
import greed.model.*;

import java.util.List;
import java.util.Locale;

/**
 * Support for the python language.
 *
 * @since 1.5
 * @author Jongwook Choi (wook)
 *
 */
public class PythonLanguage extends AbstractLanguage implements LanguageRenderer, LanguageTrait {

    public static final PythonLanguage instance = new PythonLanguage();

    protected PythonLanguage() {
    }

    @Override
    public String getCommentPrefix() {
        return "#";
    }

    @Override
    public String renderPrimitive(Primitive primitive) {
        // python has no type, this result is only for showing
        // DO NOT RENDER TYPES IN PYTHON CODE
        switch (primitive) {
            case INT:
            case LONG:
                return "integer";
            case BOOL:
                return "bool";
            case DOUBLE:
                return "float";
            case STRING:
                return "string";
        }
        return "";
    }

    @Override
    public String renderType(Type type) {
        // python has no type, this result is only for showing
        // DO NOT RENDER TYPES IN PYTHON CODE
        String res = renderPrimitive(type.getPrimitive());
        if (type.isArray())
            res = "tuple(" + res + ")";
        return res;
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

    @Override
    public String renderMethod(Method method) {
        return "def " + method.getName() + "(self, " + renderParamList(method.getParams()) + ")";
    }

    @Override
    public List<NamedRenderer> getOtherRenderers() {
        List<NamedRenderer> result = super.getOtherRenderers();
        result.add(new NamedRenderer() {
            @Override
            public String render(Object typeObj, String param, Locale locale) {
                if (typeObj instanceof Type) {
                    Type type = (Type) typeObj;
                    switch (type.getPrimitive()) {
                        case STRING:
                            return param;
                        case BOOL:
                            return param + " == True";
                        case INT:
                        case LONG:
                            return "int(" + param + ")";
                        case DOUBLE:
                            return "float(" + param + ")";
                    }
                }
                return "";
            }

            @Override
            public String getName() {
                return "parser";
            }

            @Override
            public RenderFormatInfo getFormatInfo() {
                return null;
            }

            @Override
            public Class<?>[] getSupportedClasses() {
                return new Class<?>[] { Type.class };
            }
        });
        return result;
    }
}
