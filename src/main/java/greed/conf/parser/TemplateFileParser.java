package greed.conf.parser;

import greed.util.ResourcePath;
import greed.util.Configuration;

/**
 * Greed is good! Cheers!
 */
public class TemplateFileParser extends ExpressionParser<ResourcePath> {
    @Override
    public ResourcePath raw(String templatePath) {
        return new ResourcePath(templatePath, false);
    }

    protected ResourcePath builtin(String templatePath) {
        return new ResourcePath(Configuration.TEMPLATE_PATH + "/" + templatePath, true);
    }

    @Override
    public Class<ResourcePath> typeOf1() {
        return ResourcePath.class;
    }
}
