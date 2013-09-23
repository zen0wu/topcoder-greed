package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Param {
    private String name;
    private Type type;
    private int index;

    public Param(String name, Type type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
    
    public int getIndex() {
        return index;
    }
}
