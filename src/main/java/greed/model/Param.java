package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Param {
    private String name;
    private Type type;

    public Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
