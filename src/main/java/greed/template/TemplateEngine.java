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
    private Engine engine = null;
    private HashMap<String, NamedRenderer> namedRenderers = new HashMap<String, NamedRenderer>();
    // Black magic for the renderer to access the current model
    private Stack<Map<String, Object>> modelStack = new Stack<Map<String, Object>>();

    public TemplateEngine() {
        engine = new Engine();
        this.registerNamedRenderer(new StringUtilRenderer());
        this.registerNamedRenderer(new ContestCategoryRenderer());
        this.registerNamedRenderer(new HTMLRenderer());
        this.registerNamedRenderer(new SeqRenderer());
    }

    public TemplateEngine(Language language) {
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

    private void registerNamedRenderer(NamedRenderer namedRenderer) {
        namedRenderers.put(namedRenderer.getName(), namedRenderer);
        engine.registerNamedRenderer(namedRenderer);
    }

    /**
     * A seq renderer inside each engine, allowing engine transforming iteration
     */
    private class SeqRenderer implements NamedRenderer {
        private Random random = new Random(System.currentTimeMillis());

        @Override
        public String render(Object o, String s, Locale locale) {
            if (s == null || "".equals(s)) {
                throw new IllegalArgumentException("params of seq() cannot be empty");
            }
            String iter = null;
            for (String rn: parseArgs(s)) {
                String name = rn.trim();
                if (name.startsWith("$")) {
                    // $ sees the current string and render it to the value in the model with a possible named renderer
                    // For example, ${xxx;seq(b, $c(x,y), a)}, `b` transform `xxx` into a string called `k1`,
                    // then `$c(x,y)` will call `${k1;c(x,y)}`.
                    if (iter == null) {
                        throw new IllegalArgumentException("$ cannot be the first argument of seq");
                    }
                    String realExp = name.substring(1);
                    if (realExp.length() > 0) {
                        realExp = ";" + realExp;
                    }
                    iter = TemplateEngine.this.noStackRender("${" + iter + realExp + "}");
                }
                else {
                    // Otherwise call the engine with corresponding renderer
                    // `#` means no specific renderer, and others are just named renderers
                    String renderer = name;
                    if (renderer.equals("#"))
                        renderer = "";
                    else
                        renderer = ";" + renderer;
                    // The way to achieve this is to generate a random key, bind the current object to that key,
                    // then call the engine with the generated key
                    Map<String, Object> model = new HashMap<String, Object>(TemplateEngine.this.modelStack.peek());
                    String key = randomKey();
                    model.put(key, iter == null ? o : iter);
                    iter = TemplateEngine.this.render("${" + key + renderer + "}", model);
                }
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

        private String randomKey() {
            return "KEY" + random.nextLong();
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
