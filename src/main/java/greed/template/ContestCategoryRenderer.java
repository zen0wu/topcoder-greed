package greed.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

import java.util.Locale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import greed.model.Contest;
/**
 * Greed is good! Cheers!
 */
 
/*
   Planned parameter behavior:
   
   No parameters : Uses "TCO", "SRM", "TCCC", "TCHS" and "Other"
   "srm=X": Instead of "SRM" it uses "SRM A-B" intervals A-B have length X.
             If X = 1, then it does "SRM N".
             
   TODO "tco=X": Separates TCO problems by year. When X = 1, "TCO 2012", 
                  "TCO 2013", etc. When X = 4, "TCO 2010-2014", etc.          
   TODO "tccc=X", same as TCO.
   TODO "tchs-srm=X", detects TCHS srms and does what SRM does.
   TODO "tchs-tournament=X", Behaves like TCO but with TCHS tournament.
   
   Combining parameters: "srm=25 tco=1"
                  

*/ 
public class ContestCategoryRenderer implements NamedRenderer {

    private String renderContest( Contest c, String param)
    {
        int srmSeparate = -1;
        if (param != null) {
            for (String par: param.split(" ")) {
                if ( par.startsWith("srm=") ) {
                    srmSeparate = 25;
                    try {
                        String s = par.trim().substring("srm=".length());
                        srmSeparate = Integer.parseInt(s); 
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        }
        String result = c.getName();
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
                if (srmSeparate <= 0) {
                    result = "SRM";
                } else if (srmSeparate == 1) {
                    result = "SRM "+n;
                } else {                    
                    int a = n - n % srmSeparate;
                    int b = a + srmSeparate - 1;
                    result = "SRM "+a+"-"+b;
                }
            } else {
                result = "Other";
            }
        }
        return result;
    }
    
    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof Contest) {
            return renderContest( (Contest) o, param );
        }
        return "";
    }
    
    @Override
    public String getName() {
        return "category";
    }
    
    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[]{Contest.class};
    }

    
}