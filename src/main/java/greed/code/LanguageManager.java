package greed.code;

import greed.model.Language;

import java.util.HashMap;
import java.util.Map;

/**
 * Greed is good! Cheers!
 */
public class LanguageManager {
	private static LanguageManager instance = null;

	public static LanguageManager getInstance() {
		if (instance == null) instance = new LanguageManager();
		return instance;
	}

	private Map<Language, LanguageTrait> traitMap = new HashMap<Language, LanguageTrait>();
	private Map<Language, LanguageRenderer> rendererMap = new HashMap<Language, LanguageRenderer>();
	private Map<Language, CodeProcessor> processorMap = new HashMap<Language, CodeProcessor>();

	private LanguageManager() {
		traitMap.put(Language.CPP, CStyleLanguageTrait.getInstance());
		traitMap.put(Language.JAVA, CStyleLanguageTrait.getInstance());
		traitMap.put(Language.CSHARP, CStyleLanguageTrait.getInstance());

		rendererMap.put(Language.CPP, CppRenderer.instance);
		rendererMap.put(Language.JAVA, JavaRenderer.instance);
		rendererMap.put(Language.CSHARP, CSharpRenderer.instance);

		processorMap.put(Language.JAVA, new JavaCodeProcessor());
	}

	public LanguageTrait getTrait(Language language) {
		return traitMap.get(language);
	}

	public LanguageRenderer getRenderer(Language language) {
		return rendererMap.get(language);
	}

	public CodeProcessor getProcessor(Language language) {
		return processorMap.get(language);
	}
}
