package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Method {
    private String name;
    private Type returnType;
    private Param[] params;

    public Method(String name, Type returnType, Param[] params) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Param[] getParams() {
        return params;
    }
}
