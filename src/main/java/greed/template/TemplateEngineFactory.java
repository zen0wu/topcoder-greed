package greed.template;

import com.floreysoft.jmte.Renderer;
import greed.code.LanguageManager;
import greed.model.Language;
import greed.model.ParamValue;

import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class TemplateEngineFactory {
    private static TemplateEngine bare = new TemplateEngine();

    public static TemplateEngine getBareEngine() {
        return bare;
    }

    public static TemplateEngine newLanguageEngine(Language language) {
        TemplateEngine engine = new TemplateEngine();
        LanguageManager.getInstance().registerRenderer(language, engine.getEngine());
        return engine;
    }

    public static TemplateEngine newDataEngine() {
        TemplateEngine engine = new TemplateEngine();
        engine.getEngine().registerRenderer(ParamValue.class, new Renderer<ParamValue>() {
            @Override
            public String render(ParamValue paramValue, Locale locale) {
                return paramValue.getValue();
            }
        });
        return engine;
    }

    public static TemplateEngine newSpecialEngine(String name) {
        if ("data".equals(name))
            return newDataEngine();
        throw new IllegalArgumentException("No engine named " + name);
    }
}
