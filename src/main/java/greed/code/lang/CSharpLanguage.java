package greed.code.lang;

import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

/**
 * Greed is good! Cheers!
 */
public class CSharpLanguage extends CStyleLanguage {
    public static final CSharpLanguage instance = new CSharpLanguage();

    protected CSharpLanguage() {
        super();
    }

    @Override
    public String renderPrimitive(Primitive primitive) {
        switch (primitive) {
            case STRING:
                return "string";
            case DOUBLE:
                return "double";
            case INT:
                return "int";
            case BOOL:
                return "bool";
            case LONG:
                return "long";
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
        if (paramType.isArray()) {
            return value;
        }

        if(paramType.getPrimitive() == Primitive.LONG) {
            value += "L";
        }
        return value;
    }

    @Override
    public String renderZeroValue(Type type) {
        if (type.isArray()) return "new " + renderPrimitive(type.getPrimitive()) + "[] { }";
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
}
