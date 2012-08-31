package greed.code;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.Renderer;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Primitive;
import greed.model.Type;

import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class JavaRenderer extends CppRenderer {
	public static JavaRenderer instance = new JavaRenderer();

	protected JavaRenderer() { super(); }

	public void registerSelf(Engine engine) {
		super.registerSelf(engine);
		engine.registerRenderer(Primitive.class, new Renderer<Primitive>() {
			@Override
			public String render(Primitive primitive, Locale locale) {
				return renderPrimitive(primitive);
			}
		});
		engine.registerRenderer(Type.class, new Renderer<Type>() {
			@Override
			public String render(Type type, Locale locale) {
				return renderType(type);
			}
		});
		engine.registerRenderer(Param[].class, new Renderer<Param[]>() {
			@Override
			public String render(Param[] params, Locale locale) {
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < params.length; ++i) {
					if (i > 0) buf.append(", ");
					buf.append(renderParam(params[i]));
				}
				return buf.toString();
			}
		});
		engine.registerNamedRenderer(new NamedRenderer() {
			@Override
			public String render(Object o, String s, Locale locale) {
				if (o instanceof Type) {
					Type t = (Type)o;
					if (t.isArray()) return "new " + renderPrimitive(t.getPrimitive()) + "[0]";
					switch (t.getPrimitive()) {
						case BOOL: return "false";
						case STRING: return "\"\"";
						case INT:case LONG: return "0";
						case DOUBLE: return "0.0";
					}
				}
				return "";
			}

			@Override
			public String getName() {
				return "ZeroValue";
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
	}

	@Override
	protected String renderPrimitive(Primitive o) {
		switch (o) {
			case STRING: return "String";
			case LONG: return "long";
			case BOOL: return "boolean";
			default: return super.renderPrimitive(o);
		}
	}

	@Override
	protected String renderType(Type o) {
		String typeName = renderPrimitive(o.getPrimitive());
		if (o.isArray()) typeName += "[]";
		return typeName;
	}

	@Override
	protected String renderParamValue(ParamValue o) {
		Type paramType = o.getParam().getType();
		String value = o.getValue();
		if (paramType.isArray()) {
			return value;
		}
		return value;
	}
}
