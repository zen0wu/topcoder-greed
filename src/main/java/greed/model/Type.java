package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Type {
    private final Primitive primitive;
    private final int dim;

    Type(Primitive primitive, int dim) {
        this.primitive = primitive;
        this.dim = dim;
    }


    public static Type primitiveType(Primitive primitive) {
        switch(primitive) {
            case BOOL: return BOOL_TYPE;
            case INT: return INT_TYPE;
            case STRING: return STRING_TYPE;
            case LONG: return LONG_TYPE;
            case DOUBLE: return DOUBLE_TYPE;
        }
        throw new IllegalArgumentException("Unknown primitive : " + primitive);
    }

    /* some pre-defined types */
    public static final Type INT_TYPE = new Type(Primitive.INT, 0);
    public static final Type STRING_TYPE = new Type(Primitive.STRING, 0);
    public static final Type LONG_TYPE = new Type(Primitive.LONG, 0);
    public static final Type DOUBLE_TYPE = new Type(Primitive.DOUBLE, 0);
    public static final Type BOOL_TYPE = new Type(Primitive.BOOL, 0);

    public static final Type INT_ARRAY_TYPE = new Type(Primitive.INT, 1);
    public static final Type STRING_ARRAY_TYPE = new Type(Primitive.STRING, 1);
    public static final Type LONG_ARRAY_TYPE = new Type(Primitive.LONG, 1);
    public static final Type DOUBLE_ARRAY_TYPE = new Type(Primitive.DOUBLE, 1);
    public static final Type BOOL_ARRAY_TYPE = new Type(Primitive.BOOL, 1);


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
