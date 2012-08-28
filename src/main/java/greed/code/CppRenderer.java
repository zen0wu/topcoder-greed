package greed.code;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.Renderer;
import greed.model.Primitive;
import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Type;

import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class CppRenderer implements LanguageRenderer {
    public static CppRenderer instance = new CppRenderer();

    private CppRenderer() {}

    public void registerSelf(Engine engine) {
        engine.registerRenderer(Primitive.class, instance.new PrimitiveRender());
        engine.registerRenderer(Type.class, instance.new TypeRenderer());
        engine.registerRenderer(Param.class, instance.new ParamRenderer());
        engine.registerRenderer(Param[].class, instance.new ParamListRenderer());
        engine.registerRenderer(ParamValue.class, instance.new ParamValueRenderer());
        engine.registerNamedRenderer(instance.new ZeroValueRenderer());
    }

    private static String renderPrimitive(Primitive o) {
        switch (o) {
            case STRING: return "string";
            case DOUBLE: return "double";
            case INT:    return "int";
            case BOOL:   return "bool";
            case LONG:   return "long long";
        }
        return "";
    }

    private static String renderType(Type o) {
        String typeName = renderPrimitive(o.getPrimitive());
        if (o.isArray())
            typeName = "vector<" + typeName + ">";
        return typeName;
    }

    private static String renderParam(Param o) {
        return renderType(o.getType()) + " " + o.getName();
    }

    private static String renderParamValue(ParamValue o) {
        Type paramType = o.getParam().getType();
        String value = o.getValue();
        if (paramType.isArray()) {
	        return value;
        }

        switch (paramType.getPrimitive()) {
            case STRING: value = "\"" + value + "\""; break;
            case LONG: value += "LL"; break;
        }
        return value;
    }

    class PrimitiveRender implements Renderer<Primitive> {
        @Override
        public String render(Primitive o, Locale locale) {
            return renderPrimitive(o);
        }
    }

    class ParamRenderer implements Renderer<Param> {
        @Override
        public String render(Param o, Locale locale) {
            return renderParam(o);
        }
    }

    class ParamListRenderer implements Renderer<Param[]> {
        @Override
        public String render(Param[] o, Locale locale) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < o.length; ++i) {
                if (i > 0) buf.append(", ");
                buf.append(renderParam(o[i]));
            }
            return buf.toString();
        }
    }

    class TypeRenderer implements Renderer<Type> {
        @Override
        public String render(Type o, Locale locale) {
            return renderType(o);
        }
    }

    class ParamValueRenderer implements Renderer<ParamValue> {
        @Override
        public String render(ParamValue o, Locale locale) {
            return renderParamValue(o);
        }
    }

    class ZeroValueRenderer implements NamedRenderer {
        @Override
        public String render(Object o, String format, Locale locale) {
            if (o instanceof Type) {
                Type t = (Type)o;
                if (t.isArray()) return renderType(t) + "()";
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
    }
}
