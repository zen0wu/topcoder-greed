package greed.conf.schema;

/**
 * Greed is good! Cheers!
 */
public abstract class TemplateDependencyConfig {
    public static abstract class Dependency {}

    public static class KeyDependency extends Dependency {
        private final String key;

        public KeyDependency(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static class TemplateDependency extends Dependency {
        private final String template;

        public TemplateDependency(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }
    }

    public static class OneOfDependency extends Dependency {
        private final Dependency[] dependencies;

        public OneOfDependency(Dependency[] dependencies) {
            this.dependencies = dependencies;
        }

        public Dependency[] getDependencies() {
            return dependencies;
        }
    }
}
