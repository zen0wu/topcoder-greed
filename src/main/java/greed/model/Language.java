package greed.model;

/**
 * Greed is good! Cheers!
 */
public enum Language {
    CPP, JAVA, VB, PYTHON, CSHARP;

    public static String getName(Language language) {
        if (language == Language.CPP) return "cpp";
        if (language == Language.JAVA) return "java";
        if (language == Language.CSHARP) return "csharp";
        if (language == Language.PYTHON) return "python";
        if (language == Language.VB) return "vb";
        return null;
    }

    public static Language fromString(String langName) {
        for (Language lang: Language.values())
            if (langName.equals(getName(lang)))
                return lang;
        return null;
    }
}
