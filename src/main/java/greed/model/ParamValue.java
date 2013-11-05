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
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        String sep = "";
        for (String s : valueList) {
            buf.append(sep);
            sep = ", ";
            buf.append(s);
        }
        buf.append(" }");
        this.value = buf.toString();
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
    
    public int getValueListLength() {
        return valueList.length;
    }

    public boolean isMultiLine() {
        return valueList.length > 1;
    }
}
