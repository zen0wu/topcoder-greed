package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Type {
    private Primitive primitive;
    private int dim;

    public Type(Primitive primitive, int dim) {
        this.primitive = primitive;
        this.dim = dim;
    }

    public Primitive getPrimitive() {
        return primitive;
    }

    public int getDim() {
        return dim;
    }

    public boolean isArray() {
        return dim > 0;
    }

    public boolean isString() {
        return this.primitive == Primitive.STRING;
    }

    public boolean isRealNumber() {
        return this.primitive == Primitive.DOUBLE;
    }

    public boolean isLongInteger() {
        return this.primitive == Primitive.LONG;
    }

}
