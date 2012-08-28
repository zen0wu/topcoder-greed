package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Problem {
    private String name;
    private int score;
    private String className;
    private Method method;
    private Testcase[] testcases;

    public Problem(String name, int score, String className, Method method, Testcase[] testcases) {
        this.name = name;
        this.score = score;
        this.className = className;
        this.method = method;
        this.testcases = testcases;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getClassName() {
        return className;
    }

    public Method getMethod() {
        return method;
    }

    public Testcase[] getTestcases() {
        return testcases;
    }
}
