package greed.code;

/**
 * Greed is good! Cheers!
 */
public class JavaCodeProcessor implements CodeProcessor {
	@Override
	public CodeByLine process(CodeByLine input) {
		CodeByLine code = new CodeByLine();

		// remove package declaration
		for (String line: input.getLines()) {
			if (line.trim().startsWith("package")) continue;
			code.getLines().add(line);
		}

		return code;
	}
}
