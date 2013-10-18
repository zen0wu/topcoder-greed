package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * Greed is good! Cheers!
 */
public class AppendingTransformer implements CodeTransformer {

    private final String text;

    public AppendingTransformer(String text) {
        this.text = text;
    }

    @Override
    public CodeByLine transform(CodeByLine input) {
        CodeByLine res = new CodeByLine(input);
        res.getLines().add(text);
        return res;
    }
}
