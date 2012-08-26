package greed.model;

public class Testcase {
    private int num;
    private ParamValue[] input;
    private ParamValue output;

    public Testcase(int num, ParamValue[] input, ParamValue output) {
        this.num = num;
        this.input = input;
        this.output = output;
    }

    public ParamValue[] getInput() {
        return input;
    }

    public int getNum() {
        return num;
    }

    public ParamValue getOutput() {
        return output;
    }
}
