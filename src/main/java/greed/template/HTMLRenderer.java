package greed.template;

import greed.model.ParamValue;
import greed.model.Type;
import greed.model.Language;
import greed.util.StringUtil;
import greed.code.LanguageManager;
import greed.code.LanguageRenderer;

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

    Language currentLang = null;
    
    HTMLRenderer(Language lang)
    {
        currentLang = lang;
    }
    
    private String stripHTML(String v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length(); i++) {
            switch (v.charAt(i)) {
            case '"':
                sb.append("&quot;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '\'':
                sb.append("&apos;");
                break;
            default:
                sb.append(v.charAt(i));
            } 
        }
        return sb.toString();
    }
    
    private String renderType(Type ty, String langName)
    {
        Language lang = currentLang;
        if (langName != null) {
            for (Language iteratedLang : Language.values()) {
                if (Language.getName(iteratedLang).equals(langName)) {
                    lang = iteratedLang;
                }
            }
        }
        LanguageRenderer renderer = LanguageManager.getInstance()
            .getRenderer(lang);
        String s = renderer.renderType(ty);
        return stripHTML(s);
        
    }

    private String renderParamValue(ParamValue pv, String param) {
        Type t = pv.getParam().getType();

        if (t.isString()) {
            if (t.isArray()) {
                String[] x = pv.getValueList();
                boolean useGrid = isGridMode(param, x);
                return doRenderStringArray(x, useGrid);
            } else {
                return stripHTML(pv.getValue());
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
            xQuoted[i] = stripHTML(x[i]);
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
        } else if (o instanceof Type) {
            return renderType( (Type)o, param);
        } else if (o instanceof String) {
            return stripHTML( (String)o );
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
        return new Class<?>[]{ ParamValue.class, String.class, Type.class };
    }
}
