package greed.model;

/**
 * Greed is good! Cheers!
 */
public class Problem {
    private String name;

    private int score;
    private String className;
    private Method method;
    private int memoryLimitMB;
    private int timeLimitMillis;
    private boolean hasCustomChecker;
    private ProblemDescription description;    
    
    private Testcase[] testcases;
    
    public Problem(String name, int score, String className, int memoryLimitMB, int timeLimitMillis, boolean hasCustomChecker, Method method, Testcase[] testcases, ProblemDescription description) {
        this.name = name;
        this.score = score;
        this.className = className;
        this.method = method;
        this.testcases = testcases;
        this.description = description;
        this.memoryLimitMB = memoryLimitMB;
        this.timeLimitMillis = timeLimitMillis;
        this.hasCustomChecker = hasCustomChecker;
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
    
    public int getMemoryLimitMB()
    {
        return memoryLimitMB;
    }

    public int getTimeLimitMillis()
    {
        return timeLimitMillis;
    }

    public double getTimeLimitSeconds()
    {
        return (double)timeLimitMillis / 1000.0;
    }
    
    public boolean getHasCustomChecker()
    {
        return hasCustomChecker;
    }
    
    
    public Method getMethod() {
        return method;
    }

    public Testcase[] getTestcases() {
        return testcases;
    }

    public ProblemDescription getDescription() {
        return description;
    }
}
