package greed.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

import java.util.Locale;
import java.lang.StringBuilder;

import greed.model.Param;
import greed.model.ParamValue;
import greed.model.Param;
import greed.model.Type;

/**
 * Greed is good! Cheers!
 */
public class HTMLRenderer implements NamedRenderer {
    
    private String stripQuotes(String v) {
        if (v.length() >= 2 && v.charAt(0) == '"' && v.charAt(v.length()-1) == '"') {
            v = "&quot;" + v.substring(1, v.length() - 1) + "&quot;";
        }
        return v;
    }
    
    private String renderParamValue(ParamValue pv, String param) {
        Type t = pv.getParam().getType();
        if (t.isString()) {
            if (t.isArray()) {
                String[] x = pv.getValueList();
                boolean grid = ( (x. length > 1) && (param != null) && param.equals("grid") );
                if (grid) {
                    int s = x[0].length();
                    boolean good = true;
                    for (String y: x) {
                        grid = (grid && (s == y.length()) );
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (int i = 0; i < x.length; i++) {
                    if (i != 0) {
                        if (grid) {
                            sb.append(",<br />&nbsp;");
                        } else {
                            sb.append(", ");
                        }
                    } else if (! grid) {
                        sb.append(" ");
                    }
                    sb.append(stripQuotes(x[i]));
                }
                if (! grid) {
                    sb.append(" ");
                }
                sb.append("}");
                return sb.toString();
            } else {
                return stripQuotes(pv.getValue());
            }
        }
        return pv.getValue();
    }
    
    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof ParamValue) {
            return renderParamValue( (ParamValue)o, param);
        } else if (o instanceof String) {
            return stripQuotes( (String)o );
        }
        return "(No HTML renderer support for object: "+o.toString()+")";
    }

    @Override
    public String getName() {
        return "html";
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[]{ParamValue.class, String.class};
    }
}
