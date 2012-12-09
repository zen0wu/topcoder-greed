package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * Greed is good! Cheers!
 */
public class JavaPackageRemover implements CodeTransformer {
    @Override
    public CodeByLine transform(CodeByLine input) {
        CodeByLine code = new CodeByLine();

        // remove package declaration
        for (String line : input.getLines()) {
            if (line.trim().startsWith("package")) continue;
            code.getLines().add(line);
        }

        return code;
    }
}
