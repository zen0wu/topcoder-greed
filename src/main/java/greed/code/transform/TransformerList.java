package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * @author WuCY
 */
public class TransformerList implements CodeTransformer {

    private final CodeTransformer[] transformers;

    public TransformerList(CodeTransformer... transformers) {
        this.transformers = transformers;
    }

    @Override
    public CodeByLine transform(CodeByLine input) {
        for (CodeTransformer transformer: transformers)
            input = transformer.transform(input);
        return input;
    }
}
