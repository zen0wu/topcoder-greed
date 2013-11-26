package greed.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Greed is good! Cheers!
 */
public class StringUtilRenderer implements NamedRenderer {
    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof String) {
            String result = (String) o;
            for (String func: param.split(",")) {
                if (func.trim().equals("lower")) {
                    result = result.toLowerCase();
                }
                else if (func.trim().equals("upfirst")) {
                    if (result.length() > 0)
                        result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
                }
                else if (func.trim().equals("removespace")) {
                    result = result.replaceAll("\\s", "");
                }
                else if (func.trim().equals("unquote")) {
                    if (result.length() >= 2 && result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"')
                        result = result.substring(1, result.length() - 1);
                }
                else if (func.trim().equals("abbr")) {
                    String[] tokens = result.split("\\s+");
                    StringBuilder abbr = new StringBuilder();
                    for (String tok: tokens) {
                        if (allDigits(tok) || allUppercaseOrDigits(tok))
                            abbr.append(tok);
                        else
                            abbr.append(tok.substring(0, 1).toUpperCase());
                    }
                    result = abbr.toString();
                } else if (func.trim().startsWith("contestcategory")) {
                    int separate = 25;
                    try {
                        String s = func.trim().substring("contestcategory".length());
                        separate = Integer.parseInt(s); 
                    } catch (NumberFormatException nfe) {
                    }
                    if (result.contains("TCHS")) {
                        result = "TCHS";
                    } else if (result.matches("(?i).*(TCO|(top\\s*coder\\s*open)).*")) {
                        result = "TCO";
                    } else if (result.contains("TCCC")) {
                        result = "TCCC";
                    } else {
                        String pattern = "(?i).*(SRM|(single\\s*round\\s*match))\\s*(\\d+).*";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(result);
                        if (m.find( )) {
                            int n = 0;
                            try {
                                n = Integer.parseInt( m.group(3) );
                            } catch (NumberFormatException nfe) {
                            }
                            int a = n - n % separate;
                            int b = a + separate - 1;
                            result = "SRM "+a+"-"+b;
                        } else {
                            result = "Other";
                        }
                    }
                }
            }
            return result;
        }
        return "";
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
        for (int i = 0; i < s.length(); ++i)
            if (!Character.isUpperCase(s.charAt(i)) && !Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[]{String.class};
    }
}
