package greed.code;

import greed.code.lang.CSharpLanguage;
import greed.code.lang.CppLanguage;
import greed.code.lang.JavaLanguage;
import greed.code.lang.PythonLanguage;
import greed.code.transform.JavaPackageRemover;
import greed.model.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.Renderer;

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
    private Map<Language, CodeTransformer> postTransformerMap = new HashMap<Language, CodeTransformer>();

    private LanguageManager() {
        traitMap.put(Language.CPP, CppLanguage.instance);
        traitMap.put(Language.JAVA, JavaLanguage.instance);
        traitMap.put(Language.CSHARP, CSharpLanguage.instance);
        traitMap.put(Language.PYTHON, PythonLanguage.instance);

        rendererMap.put(Language.CPP, CppLanguage.instance);
        rendererMap.put(Language.JAVA, JavaLanguage.instance);
        rendererMap.put(Language.CSHARP, CSharpLanguage.instance);
        rendererMap.put(Language.PYTHON, PythonLanguage.instance);

        postTransformerMap.put(Language.JAVA, new JavaPackageRemover());
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
            engine.registerRenderer(Method.class, new Renderer<Method>() {
                @Override
                public String render(Method method, Locale locale) {
                    return renderer.renderMethod(method);
                }
            });
            for (NamedRenderer namedRenderer: renderer.getOtherRenderers())
                engine.registerNamedRenderer(namedRenderer);
        }
    }

    public LanguageTrait getTrait(Language language) {
        return traitMap.get(language);
    }

    public CodeTransformer getPostTransformer(Language language) {
        return postTransformerMap.get(language);
    }

    public LanguageRenderer getRenderer(Language language) {
        return rendererMap.get(language);
    }
}
