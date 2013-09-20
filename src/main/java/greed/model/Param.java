package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Param {
    private String name;
    private Type type;
    private int num;

    public Param(String name, Type type, int num) {
        this.name = name;
        this.type = type;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
    
    public int getNum() {
        return num;
    }
}
