package greed.code;

import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

/**
 * Greed is good! Cheers!
 */
public class CppRenderer implements LanguageRenderer {
    public static CppRenderer instance = new CppRenderer();

    protected CppRenderer() {
    }

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
                return "long long";
        }
        return "";
    }

    public String renderType(Type type) {
        String typeName = renderPrimitive(type.getPrimitive());
        if (type.isArray())
            typeName = "vector<" + typeName + ">";
        return typeName;
    }

    public String renderParam(Param param) {
        return renderType(param.getType()) + " " + param.getName();
    }

    public String renderParamValue(ParamValue paramValue) {
        Type paramType = paramValue.getParam().getType();
        String value = paramValue.getValue();
        if (paramType.isArray()) {
            return value;
        }

        switch (paramType.getPrimitive()) {
            case LONG:
                value += "LL";
                break;
        }
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
        if (type.isArray()) return renderType(type) + "()";
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
