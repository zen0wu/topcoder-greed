package greed.code;

import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

/**
 * Greed is good! Cheers!
 */
public class JavaRenderer extends CppRenderer {
	public static JavaRenderer instance = new JavaRenderer();

	protected JavaRenderer() { super(); }

	@Override
	public String renderPrimitive(Primitive primitive) {
		switch (primitive) {
			case STRING: return "String";
			case LONG: return "long";
			case BOOL: return "boolean";
			default: return super.renderPrimitive(primitive);
		}
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
            case BOOL: return "false";
            case STRING: return "\"\"";
            case INT:case LONG: return "0";
            case DOUBLE: return "0.0";
        }
        return "";
    }
}
