package greed.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

import java.util.Locale;

/**
 * Greed is good! Cheers!
 */
public class StringUtilRenderer implements NamedRenderer {
	@Override
	public String render(Object o, String param, Locale locale) {
		if (o instanceof String) {
			String result = (String) o;
			if (param.toLowerCase().contains("lower"))
				result = result.toLowerCase();
			if (param.toLowerCase().contains("removespace"))
				result = result.replaceAll("\\s", "");
			return result;
		}
		return "";
	}

	@Override
	public String getName() {
		return "string";
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
