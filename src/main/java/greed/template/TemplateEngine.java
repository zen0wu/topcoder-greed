package greed.template;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import greed.code.LanguageManager;
import greed.model.Language;
import greed.util.FileSystem;

import java.io.InputStream;
import java.util.*;

/**
 * Greed is good! Cheers!
 */
public class TemplateEngine {

    private static TemplateEngine bare = new TemplateEngine();

    public static TemplateEngine getBareEngine() { return bare; }

    public static TemplateEngine newLanguageEngine(Language lang) {
        return new TemplateEngine(lang);
    }

    private Engine engine = null;
    // Black magic for the renderer to access the current model
    private Stack<Map<String, Object>> modelStack = new Stack<Map<String, Object>>();

    private TemplateEngine() {
        engine = new Engine();
        engine.registerNamedRenderer(new StringUtilRenderer());
        engine.registerNamedRenderer(new ContestCategoryRenderer());
        engine.registerNamedRenderer(new HTMLRenderer());
        engine.registerNamedRenderer(new ReifyRenderer());
        engine.registerNamedRenderer(new FormatRenderer());
        engine.registerNamedRenderer(new SeqRenderer());
    }

    private TemplateEngine(Language language) {
        this();
        LanguageManager.getInstance().registerRenderer(language, engine);
    }

    public String render(InputStream templateStream, Map<String, Object> model) {
        return render(FileSystem.readStream(templateStream), model);
    }

    public String render(String template, Map<String, Object> model) {
        modelStack.push(model);
        String output = noStackRender(template);
        modelStack.pop();
        return output;
    }

    private String noStackRender(String template) {
        return engine.transform(template, modelStack.peek());
    }

    /**
     * Use the input as a key and reify the result by ${key}
     */
    private class ReifyRenderer implements NamedRenderer {

        @Override
        public String render(Object o, String s, Locale locale) {
            if (o instanceof String) {
                return TemplateEngine.this.noStackRender("${" + o + "}");
            }
            throw new IllegalArgumentException("ReifyRenderer only accepts strings");
        }

        @Override
        public String getName() {
            return "reify";
        }

        @Override
        public RenderFormatInfo getFormatInfo() {
            return null;
        }

        @Override
        public Class<?>[] getSupportedClasses() {
            return new Class<?> [] { String.class };
        }
    }

    /**
     * A seq renderer inside each engine, allowing engine transforming iteration
     */
    private class SeqRenderer implements NamedRenderer {
        @Override
        public String render(Object o, String args, Locale locale) {
            if (args == null || "".equals(args)) {
                // No arguments yields default behaviour
                args = "#";
            }
            // Resolve the variables in args
            StringBuilder resolved = new StringBuilder();
            for (int i = 0; i < args.length(); ++i) {
                char c = args.charAt(i);
                if (c == '$') {
                    int j = i + 1;
                    while (j < args.length() && "(),".indexOf(args.charAt(j)) == -1)
                        j += 1;
                    String variable = args.substring(i + 1, j);
                    String resv = TemplateEngine.this.noStackRender("${" + variable + "}");
                    resolved.append(resv);
                    i = j - 1;
                }
                else resolved.append(c);
            }
            args = resolved.toString();
            // Iteration on te renderers
            String iter = null;
            for (String exp: parseArgs(args)) {
                // Call the engine with corresponding renderer
                // `#` means no specific renderer, and others are just named renderers
                String renderer = exp.trim();
                if (renderer.equals("#"))
                    renderer = "";
                else
                    renderer = ";" + renderer;
                // The way to achieve this is to generate a random key, bind the current object to that key,
                // then call the engine with the generated key
                Map<String, Object> model = new HashMap<String, Object>(TemplateEngine.this.modelStack.peek());
                String key = UUID.randomUUID().toString();
                model.put(key, iter == null ? o : iter);
                iter = TemplateEngine.this.render("${" + key + renderer + "}", model);
            }

            return iter;
        }

        private String[] parseArgs(String args) {
            ArrayList<String> parsed = new ArrayList<String>();
            int parenDepth = 0;
            StringBuilder current = new StringBuilder();
            for (int i = 0; i < args.length(); ++i) {
                char c = args.charAt(i);
                if (c == ',' && parenDepth == 0) {
                    parsed.add(current.toString());
                    current.setLength(0);
                }
                else {
                    current.append(c);
                }
                if (c == '(') {
                    parenDepth++;
                }
                else if (c == ')') {
                    parenDepth--;
                    if (parenDepth < 0)
                        throw new IllegalArgumentException("Malformed expression " + args);
                }
            }
            if (parenDepth != 0)
                throw new IllegalArgumentException("Malformed expression " + args);
            if (current.length() > 0)
                parsed.add(current.toString());
            return parsed.toArray(new String[parsed.size()]);
        }

        @Override
        public String getName() {
            return "seq";
        }

        @Override
        public RenderFormatInfo getFormatInfo() {
            return null;
        }

        @Override
        public Class<?>[] getSupportedClasses() {
            return new Class<?>[] { Object.class };
        }
    }
}
