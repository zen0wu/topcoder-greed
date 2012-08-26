package greed.model;

public class ParamValue {
    private Param param;
    private String value;
    private String[] valueList;

    public ParamValue(Param param, String value) {
        this.param = param;
        this.value = value;
        if (!param.getType().isArray())
            this.valueList = new String[] { value };
        else {
            String t = value.trim();
            t = t.substring(1, t.length() - 1).trim();
            this.valueList = t.split("\n");
        }
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
