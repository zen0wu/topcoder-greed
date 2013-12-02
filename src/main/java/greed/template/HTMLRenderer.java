package greed.template;

import greed.model.ParamValue;
import greed.model.Type;
import greed.util.StringUtil;

import java.util.Locale;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * Greed is good! Cheers!
 *
 * @author vexorian
 * @author Jongwook Choi
 */
public class HTMLRenderer implements NamedRenderer {

    // TODO strip in case of non-grid or malformed input.
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
                boolean useGrid = isGridMode(param, x);
                return doRenderStringArray(x, useGrid);
            } else {
                return stripQuotes(pv.getValue());
            }
        }
        return pv.getValue();
    }

    private boolean isGridMode(String param, String[] x) {
        boolean grid = ((x.length > 1) && "grid".equals(param));
        if (grid) {
            int s = x[0].length();
            for (String y : x) {
                grid = ( grid && (s == y.length()) );
            }
        }
        return grid;
    }

    private String doRenderStringArray(String[] x, boolean grid) {
        StringBuilder sb = new StringBuilder();
        String[] xQuoted = new String[x.length];

        for(int i = 0; i < x.length; ++ i) {
            xQuoted[i] = stripQuotes(x[i]);
        }

        sb.append("{");
        if(grid) {
            sb.append(StringUtil.join(xQuoted, ",<br />&nbsp;"));
        } else {
            sb.append(" ");
            sb.append(StringUtil.join(xQuoted, ", "));
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
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
        return new Class<?>[]{ ParamValue.class, String.class };
    }
}
