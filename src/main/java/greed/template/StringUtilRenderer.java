package greed.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * Greed is good! Cheers!
 *
 * @author Shiva wu
 * @author vexorian
 * @author Jongwook Choi
 */
public class StringUtilRenderer implements NamedRenderer {

    private List<String> parseParams(String param) {
        if(param == null) return Collections.emptyList();
        List<String> params = new ArrayList<String>();

        for(String func : param.split(",")) {
            func = func.trim();
            if(func.isEmpty()) continue;
            params.add(func.toLowerCase());
        }
        return params;
    }

    @Override
    public String render(Object o, String param, Locale locale) {
        if (!(o instanceof String))
            return "";

        String result = (String) o;
        for (String func: parseParams(param)) {
            if ("lower".equals(func)) {
                result = applyLower(result);
            }
            else if("upfirst".equals(func)) {
                // TODO awkward name.
                result = applyUpfirst(result);
            }
            else if("removespace".equals(func)) {
                result = applyRemoveSpace(result);
            }
            else if("unquote".equals(func)) {
                result = applyUnquote(result);
            }
            else if("abbr".equals(func)) {
                result = applyAbbr(result);
            }
            else if(func.startsWith("contestcategory")) {
                result = applyContestCategory(result, func);
            }
        }
        return result;
    }

    private String applyLower(String s) {
        return s.toLowerCase();
    }

    private String applyUpfirst(String s) {
        if(s.length() > 0) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return s;
    }

    private String applyRemoveSpace(String s) {
        return s.replaceAll("\\s",  "");
    }

    private String applyUnquote(String s) {
        int n = s.length();
        if (n >= 2 && s.charAt(0) == '"' && s.charAt(n - 1) == '"')
            s = s.substring(1, n - 1);
        return s;
    }

    private String applyAbbr(String s) {
        String[] tokens = s.split("\\s+");
        StringBuilder abbr = new StringBuilder();
        for (String tok : tokens) {
            if (allDigits(tok) || allUppercaseOrDigits(tok))
                abbr.append(tok);
            else
                abbr.append(tok.substring(0, 1).toUpperCase());
        }
        return abbr.toString();
    }

    private String applyContestCategory(String s, String func) {
        int separate = 25;
        try {
            String len = func.substring("contestcategory".length());
            if(! len.isEmpty())
                separate = Integer.parseInt(len);
        } catch (NumberFormatException nfe) {
        }

        if (s.contains("TCHS")) {
            return "TCHS";
        } else if (s.matches("(?i).*(TCO|(top\\s*coder\\s*open)).*")) {
            return "TCO";
        } else if (s.contains("TCCC")) {
            return "TCCC";
        } else {
            String pattern = "(?i).*(SRM|(single\\s*round\\s*match))\\s*(\\d+).*";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(s);
            if (m.find( )) {
                int n = 0;
                try {
                    n = Integer.parseInt( m.group(3) );
                } catch (NumberFormatException nfe) {
                }
                int a = n - n % separate;
                int b = a + separate - 1;
                s = "SRM " + a + "-" + b;
            } else {
                s = "Other";
            }
            return s;
        }
    }

    @Override
    public String getName() {
        return "string";
    }

    private boolean allDigits(String s) {
        for (int i = 0; i < s.length(); ++i)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    private boolean allUppercaseOrDigits(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isUpperCase(s.charAt(i)) && !Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
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
