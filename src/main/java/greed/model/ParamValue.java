package greed.model;

/**
 * Greed is good! Cheers!
 */
public class ParamValue {
    private Param param;
    private String value;

    public ParamValue(Param param, String value) {
        this.param = param;
        this.value = value;
    }

    public Param getParam() {
        return param;
    }

    public String getValue() {
        return value;
    }

    public String[] getStrings() {
        return new String[] { value };
    }

    public ParamValue[] getValueList() {
        return new ParamValue[] { this };
    }

    public int getValueListLength() {
        return this.getValueList().length;
    }

    public boolean isMultiLine() {
        return this.getValueListLength() > 1;
    }

}
