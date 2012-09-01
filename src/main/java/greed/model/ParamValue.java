package greed.model;

/**
 * Greed is good! Cheers!
 */
public class ParamValue {
    private Param param;
    private String value;
    private String[] valueList;

    public ParamValue(Param param, String value) {
        this.param = param;
        this.value = value;
        this.valueList = new String[]{value};
    }

    public ParamValue(Param param, String[] valueList) {
        this.param = param;
        this.value = "";
        this.valueList = valueList;
    }

    public Param getParam() {
        return param;
    }

    public String getValue() {
        return value;
    }

    public String[] getValueList() {
        return valueList;
    }

    public boolean isMultiLine() {
        return valueList.length > 1;
    }
}
