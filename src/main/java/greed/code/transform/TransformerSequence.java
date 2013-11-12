package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * Greed is good! Cheers!
 */
public class TransformerSequence implements CodeTransformer {

    private final CodeTransformer[] transformers;

    public TransformerSequence(CodeTransformer... transformers) {
        this.transformers = transformers;
    }

    @Override
    public CodeByLine transform(CodeByLine input) {
        for (CodeTransformer transformer: transformers)
            input = transformer.transform(input);
        return input;
    }
}
