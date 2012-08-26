package greed.model;

public class Contest {
    private String name;
    private int div;

    public Contest(String name, int div) {
        this.div = div;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDiv() {
        return div;
    }
}
