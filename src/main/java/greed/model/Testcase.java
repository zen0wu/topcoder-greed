package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Testcase {
    private int num;
    private ParamValue[] input;
    private ParamValue output;

    private String annotation;

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

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
