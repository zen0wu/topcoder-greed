package greed.conf.schema;

import greed.conf.meta.ConfigObjectClass;
import greed.conf.meta.Optional;
import greed.conf.meta.Required;

/**
 * Greed is good! Cheers!
 */
@ConfigObjectClass
public class CommandConfig {
    @Required
    private String execute;

    @Required
    private String[] arguments;

    @Optional
    private double timeout = -1;

    public double getTimeout() {
        return timeout;
    }

    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }

    public String getExecute() {
        return execute;
    }

    public void setExecute(String execute) {
        this.execute = execute;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
