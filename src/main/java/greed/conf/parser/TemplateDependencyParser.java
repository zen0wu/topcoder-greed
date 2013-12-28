package greed.conf.parser;

import greed.conf.schema.TemplateDependencyConfig;

/**
 * Greed is good! Cheers!
 */
public class TemplateDependencyParser extends ExpressionParser<TemplateDependencyConfig.Dependency> {

    @Override
    public TemplateDependencyConfig.Dependency raw(String templateName) {
        return new TemplateDependencyConfig.TemplateDependency(templateName);
    }

    public TemplateDependencyConfig.KeyDependency key(String key) {
        return new TemplateDependencyConfig.KeyDependency(key);
    }

    @Override
    public TemplateDependencyConfig.Dependency apply(String expression) throws ReflectiveOperationException {
        String[] subExps = expression.split("\\|");
        for (int i = 0; i < subExps.length; ++i)
            subExps[i] = subExps[i].trim();
        if (subExps.length == 1) {
            return parseExp(subExps[0], TemplateDependencyConfig.Dependency.class);
        }
        else {
            TemplateDependencyConfig.Dependency[] dependencies = new TemplateDependencyConfig.Dependency[subExps.length];
            for (int i = 0; i < dependencies.length; ++i) {
                dependencies[i] = parseExp(subExps[i], TemplateDependencyConfig.Dependency.class);
            }
            return new TemplateDependencyConfig.OneOfDependency(dependencies);
        }
    }

    @Override
    public Class<TemplateDependencyConfig.Dependency> typeOf1() {
        return TemplateDependencyConfig.Dependency.class;
    }
}
