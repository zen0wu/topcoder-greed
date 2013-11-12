package greed.util;

import java.util.Collection;

/**
 * Greed is good! Cheers!
 */
public class StringUtil {
    public static String join(String[] strs, String sep) {
        StringBuilder bud = new StringBuilder();
        for (String s: strs) {
            if (bud.length() > 0)
                bud.append(sep);
            bud.append(s);
        }
        return bud.toString();
    }

    public static String join(Collection<String> strs, String sep) {
        StringBuilder bud = new StringBuilder();
        for (String s: strs) {
            if (bud.length() > 0)
                bud.append(sep);
            bud.append(s);
        }
        return bud.toString();
    }
}
