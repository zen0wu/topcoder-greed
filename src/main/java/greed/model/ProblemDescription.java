package greed.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Greed is good! Cheers!
 */
public class ProblemDescription {
    private String intro;
    private String[] notes;
    private String[] constraints;
    private String modulo;
    
    private String extractModulo(String intro)
    {
        /* d, modulo 1,000,000,007.</ */
        String pattern = "mod(ulo)? (\\d[\\d,\\.]*\\d)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(intro);
        String res = null;
        // The modulo tends to be at the end of the statement. If there were
        // multiple modulo 1,XXX,XXX,XXX statements, it is better to get the last.
        while (m.find( )) {
            try {
                 res = "" + 
                        java.text.NumberFormat.
                         getNumberInstance(java.util.Locale.US).parse(m.group(2));
            } catch (Exception e) {
            }
        }
        return res;
    }

    public ProblemDescription(String intro, String[] notes, String[] constraints) {
        this.intro = intro;
        this.notes = notes;
        this.constraints = constraints;
        this.modulo = extractModulo(intro);
    }
    
    public String getIntro() {
        return intro;
    }

    public String[] getNotes() {
        return notes;
    }
    public boolean getHasNotes() {
        return (notes.length > 0);
    }
    

    public String[] getConstraints() {
        return constraints;
    }
    
    public String getModulo() {
        return modulo;
    }
    
}
