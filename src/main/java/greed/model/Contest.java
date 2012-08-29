package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Contest {
    private String name;
    private Integer div;

    public Contest(String name, Integer div) {
        this.div = div;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getDiv() {
        return div;
    }
}
