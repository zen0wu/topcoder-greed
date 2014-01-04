package greed.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class FormatRenderer implements NamedRenderer {

    @Override
    public String render(Object o, String s, Locale locale) {
        return String.format(Locale.US, s, o);
    }

    @Override
    public String getName() {
        return "format";
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[] { String.class };
    }
}
