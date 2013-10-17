package greed.conf.schema;

import greed.conf.meta.Required;

/**
 * Greed is good! Cheers!
 */
public class CommandConfig {
    @Required
    private String execute;

    @Required
    private String[] arguments;

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
