package greed.model;

/**
 * Greed is good! Cheers!
 */
public class ProblemDescription {
    private String intro;
    private String[] notes;
    private String[] constraints;

    public ProblemDescription(String intro, String[] notes, String[] constraints) {
        this.intro = intro;
        this.notes = notes;
        this.constraints = constraints;
    }

    public String getIntro() {
        return intro;
    }

    public String[] getNotes() {
        return notes;
    }

    public String[] getConstraints() {
        return constraints;
    }
}
