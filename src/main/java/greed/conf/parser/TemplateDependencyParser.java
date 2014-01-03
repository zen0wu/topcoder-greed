package greed.conf.parser;

import greed.conf.schema.TemplateDependencyConfig.*;

/**
 * Greed is good! Cheers!
 */
public class TemplateDependencyParser extends ExpressionParser<Dependency> {

    @Override
    public Dependency raw(String templateName) {
        return new TemplateDependency(templateName);
    }

    public KeyDependency key(String key) {
        return new KeyDependency(key);
    }

    @Override
    public Dependency apply(String expression) throws ReflectiveOperationException {
        String[] subExps = expression.split("\\|");
        for (int i = 0; i < subExps.length; ++i)
            subExps[i] = subExps[i].trim();
        if (subExps.length == 1) {
            return parseExp(subExps[0], Dependency.class);
        }
        else {
            Dependency[] dependencies = new Dependency[subExps.length];
            for (int i = 0; i < dependencies.length; ++i) {
                dependencies[i] = parseExp(subExps[i], Dependency.class);
            }
            return new OneOfDependency(dependencies);
        }
    }

    @Override
    public Class<Dependency> typeOf1() {
        return Dependency.class;
    }
}
