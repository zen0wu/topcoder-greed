package greed.template;

import com.floreysoft.jmte.Engine;
import greed.code.LanguageManager;
import greed.model.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Greed is good! Cheers!
 */
public class TemplateEngine {
    private static Engine engine = null;

    private static void lazyInit() {
        if (engine == null)
            engine = new Engine();
        engine.registerNamedRenderer(new StringUtilRenderer());
        engine.registerNamedRenderer(new ContestCategoryRenderer());
    }

    public static void switchLanguage(Language language) {
        lazyInit();
        engine.registerNamedRenderer(new HTMLRenderer(language));
        LanguageManager.getInstance().registerRenderer(language, engine);
    }

    public static String render(InputStream templateStream, Map<String, Object> model) {
        lazyInit();

        String template = readStream(templateStream);
        return engine.transform(template, model);
    }

    public static String render(String template, Map<String, Object> model) {
        lazyInit();
        return engine.transform(template, model);
    }

    private static String readStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buf = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buf.append(line);
                buf.append("\n");
            }
            reader.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return buf.toString();
    }
}
