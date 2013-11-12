package greed.code.lang;

import greed.model.Param;
import greed.model.Type;


/**
 * Base implementation for C-style languages,
 * including CPP, Csharp, and Java.
 *
 * Greed is good! Cheers!
 */
public abstract class CStyleLanguage extends AbstractLanguage {

    @Override
    public String getCommentPrefix() {
        return "//";
    }

    @Override
    public String renderParam(Param param) {
        return renderType(param.getType()) + " " + param.getName();
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
