package greed.code;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.Renderer;
import greed.model.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Greed is good! Cheers!
 */
public class LanguageManager {
    private static LanguageManager instance = null;

    public static LanguageManager getInstance() {
        if (instance == null) instance = new LanguageManager();
        return instance;
    }

    private Map<Language, LanguageTrait> traitMap = new HashMap<Language, LanguageTrait>();
    private Map<Language, LanguageRenderer> rendererMap = new HashMap<Language, LanguageRenderer>();
    private Map<Language, CodeProcessor> processorMap = new HashMap<Language, CodeProcessor>();

    private LanguageManager() {
        traitMap.put(Language.CPP, CStyleLanguageTrait.getInstance());
        traitMap.put(Language.JAVA, CStyleLanguageTrait.getInstance());
        traitMap.put(Language.CSHARP, CStyleLanguageTrait.getInstance());

        rendererMap.put(Language.CPP, CppRenderer.instance);
        rendererMap.put(Language.JAVA, JavaRenderer.instance);
        rendererMap.put(Language.CSHARP, CSharpRenderer.instance);

        processorMap.put(Language.JAVA, new JavaCodeProcessor());
    }

    public LanguageTrait getTrait(Language language) {
        return traitMap.get(language);
    }

    public void registerRenderer(Language language, Engine engine) {
        final LanguageRenderer renderer = rendererMap.get(language);
        if (renderer != null) {
            engine.registerRenderer(Primitive.class, new Renderer<Primitive>() {
                @Override
                public String render(Primitive primitive, Locale locale) {
                    return renderer.renderPrimitive(primitive);
                }
            });
            engine.registerRenderer(Type.class, new Renderer<Type>() {
                @Override
                public String render(Type type, Locale locale) {
                    return renderer.renderType(type);
                }
            });
            engine.registerRenderer(Param.class, new Renderer<Param>() {
                @Override
                public String render(Param param, Locale locale) {
                    return renderer.renderParam(param);
                }
            });
            engine.registerRenderer(ParamValue.class, new Renderer<ParamValue>() {
                @Override
                public String render(ParamValue paramValue, Locale locale) {
                    return renderer.renderParamValue(paramValue);
                }
            });
            engine.registerRenderer(Param[].class, new Renderer<Param[]>() {
                @Override
                public String render(Param[] params, Locale locale) {
                    return renderer.renderParamList(params);
                }
            });
            engine.registerNamedRenderer(new NamedRenderer() {
                @Override
                public String render(Object o, String s, Locale locale) {
                    if (o instanceof Type)
                        return renderer.renderZeroValue((Type) o);
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
                    return new Class<?>[]{Type.class};
                }
            });
        }
    }

    public CodeProcessor getProcessor(Language language) {
        return processorMap.get(language);
    }
}
