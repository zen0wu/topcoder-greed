package greed.template;

import greed.model.Contest;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.EnumMap;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;

/**
 * Planned parameter behavior:
 * <p>
 * No parameters : Uses "TCO", "SRM", "TCCC", "TCHS" and "Other"
 * <p>
 * <ul>
 * <li> "srm=X": Instead of "SRM" it uses "SRM A-B" intervals A-B have length X.
 *       If X = 1, then it does "SRM N".
 * <li> "srm-text=Y" Instead of "SRM" inserts Y. Y might have spaces.
 * <li> "tco-text=Y" , "tchs-text=Y", "tccc-text=Y", "other-text=Y"
 *      Allows to rename the other categories.
 * <li> "no-space" : Remove space, currently the space between "SRM" and "A-B".
 * <li> Combining arguments example:
 *      "srm=25,no-space,srm-text=SingleRound Match,other-text=other matches"
 * <li> TODO "tco=X": Separates TCO problems by year. When X = 1, "TCO 2012",
 *                "TCO 2013", etc. When X = 4, "TCO 2010-2014", etc.
 * <li> TODO "tccc=X", same as TCO.
 * <li> TODO "tchs-srm=X", detects TCHS srms and does what SRM does.
 * <li> TODO "tchs-tournament=X", Behaves like TCO but with TCHS tournament.
 * <li> TODO Combining parameters: "srm=25 tco=1"
 * </ul>
 *
 */
public class ContestCategoryRenderer implements NamedRenderer {

    private enum CATEGORY {
        SRM, TCHS, TCO, TCCC, OTHER;
    }

    private String renderContest(Contest c, String param) {
        EnumMap<CATEGORY, String> text = new EnumMap<CATEGORY, String>(CATEGORY.class);
        text.put( CATEGORY.SRM  , "SRM"   );
        text.put( CATEGORY.TCO  , "TCO"   );
        text.put( CATEGORY.TCCC , "TCCC"  );
        text.put( CATEGORY.TCHS , "TCHS"  );
        text.put( CATEGORY.OTHER, "Other" );
        String[] namePar = new String[] {
            "srm-text=",
            "tco-text=",
            "tccc-text=",
            "tchs-text=",
            "other-text="
        };
        CATEGORY[] nameParCat = new CATEGORY[] {
            CATEGORY.SRM,
            CATEGORY.TCO,
            CATEGORY.TCCC,
            CATEGORY.TCHS,
            CATEGORY.OTHER,
        };

        int srmSeparate = -1;
        String space = " ";
        if (param != null) {
            for (String parr: param.split(",")) {
                String par = parr.trim();
                if ( par.startsWith("srm=") ) {
                    srmSeparate = 25;
                    try {
                        String s = par.substring("srm=".length());
                        srmSeparate = Integer.parseInt(s);
                    } catch (NumberFormatException nfe) {
                    }
                } else if (par.equals("no-space")) {
                    space = "";
                } else {
                    for (int i = 0; i < namePar.length; i++) {
                        if (par.startsWith( namePar[i] )) {
                            String s = par.substring( namePar[i].length() );
                            text.put( nameParCat[i], s );
                        }
                    }
                }
            }
        }
        String result = c.getName();
        if (result.contains("TCHS")) {
            result = text.get( CATEGORY.TCHS );
        } else if (result.matches("(?i).*(TCO|(top\\s*coder\\s*open)).*")) {
            result = text.get( CATEGORY.TCO );
        } else if (result.contains("TCCC")) {
            result = text.get( CATEGORY.TCCC );
        } else {
            String pattern = "(?i).*(SRM|(single\\s*round\\s*match))\\s*((\\d+)(\\.\\d+)?).*";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(result);
            if (m.find()) {
                int matchNo = 0; // only the integer part (600 for SRM 600.5)
                String matchNumber = m.group(3); // as-is (600.5 for SRM 600.5)
                try {
                    matchNo = Integer.parseInt(m.group(4));
                } catch (NumberFormatException nfe) { }
                if (srmSeparate <= 0) {
                    result = text.get( CATEGORY.SRM );
                } else if (srmSeparate == 1) {
                    // for the special case of (srm=1), preserve the fractional part
                    result = text.get( CATEGORY.SRM ) + space + matchNumber;
                } else {
                    int a = matchNo - matchNo % srmSeparate;
                    int b = a + srmSeparate - 1;
                    result = text.get( CATEGORY.SRM ) + space + a + "-" + b;
                }
            } else {
                result = text.get( CATEGORY.OTHER );
            }
        }
        return result;
    }

    @Override
    public String render(Object o, String param, Locale locale) {
        if (o instanceof Contest) {
            return renderContest((Contest) o, param);
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
        return new Class<?>[] { Contest.class };
    }

}
