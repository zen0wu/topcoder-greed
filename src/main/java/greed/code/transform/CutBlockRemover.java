package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * Greed is good! Cheers!
 */
public class CutBlockRemover implements CodeTransformer {

    private String startTag;
    private String endTag;

    public CutBlockRemover(String startTag, String endTag) {
        this.startTag = startTag;
        this.endTag = endTag;
    }

    @Override
    public CodeByLine transform(CodeByLine input) {
        CodeByLine res = new CodeByLine();

        boolean cutting = false;
        for (String line: input.getLines()) {
            if (line.trim().equals(startTag))
                cutting = true;
            else if (line.trim().equals(endTag))
                cutting = false;
            else if (!cutting)
                res.getLines().add(line);
        }

        return res;
    }
}
