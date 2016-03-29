package greed.model;

/**
 * Greed is good! Cheers!
 */
public class ParamValueList extends ParamValue {

    private ParamValue[] values;
    private String[] valueStrings;

    public ParamValueList(Param param, String[] valueList) {
        super(param, joinValues(valueList));
        assert param.getType().isArray();

        this.valueStrings = valueList;
        this.values = new ParamValue[valueList.length];
        Type elemType = new Type(param.getType().getPrimitive(), param.getType().getDim() - 1);
        Param elemParam = new Param(param.getName(), elemType, param.getIndex());
        for (int i = 0; i < values.length; ++i) {
            this.values[i] = new ParamValue(elemParam, valueList[i]);
        }
    }

    private static String joinValues(String[] valueList) {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        String sep = "";
        for (String s : valueList) {
            buf.append(sep);
            sep = ", ";
            buf.append(s);
        }
        buf.append(" }");
        return buf.toString();
    }

    public ParamValue[] getValueList() {
        return values;
    }

    @Override
    public String[] getStrings() {
        return valueStrings;
    }
}
