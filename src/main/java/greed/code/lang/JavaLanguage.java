package greed.code.lang;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

import java.util.List;
import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class JavaLanguage extends CStyleLanguage {
    public static final JavaLanguage instance = new JavaLanguage();

    protected JavaLanguage() {
        super();
    }

    @Override
    public String renderPrimitive(Primitive primitive) {
        switch (primitive) {
            case STRING:
                return "String";
            case LONG:
                return "long";
            case BOOL:
                return "boolean";
            case INT:
                return "int";
            case DOUBLE:
                return "double";
        }
        return "";
    }

    @Override
    public String renderType(Type type) {
        String typeName = renderPrimitive(type.getPrimitive());
        if (type.isArray()) typeName += "[]";
        return typeName;
    }

    @Override
    public String renderParamValue(ParamValue paramValue) {
        Type paramType = paramValue.getParam().getType();
        String value = paramValue.getValue();
        if (paramType.isArray())
            return value;
        if (paramType.getPrimitive() == Primitive.LONG)
            return value + "L";
        return value;
    }

    @Override
    public String renderZeroValue(Type type) {
        if (type.isArray()) return "new " + renderPrimitive(type.getPrimitive()) + "[0]";
        switch (type.getPrimitive()) {
            case BOOL:
                return "false";
            case STRING:
                return "\"\"";
            case INT:
            case LONG:
                return "0";
            case DOUBLE:
                return "0.0";
        }
        return "";
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
                            return "Boolean.parseBoolean(" + param + ")";
                        case INT:
                            return "Integer.parseInt(" + param + ")";
                        case LONG:
                            return "Long.parseLong(" + param + ")";
                        case DOUBLE:
                            return "Double.parseDouble(" + param + ")";
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
