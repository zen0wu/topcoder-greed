package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * @author WuCY
 */
public class BlockRemover implements CodeTransformer {

    private String startTag;
    private String endTag;

    public BlockRemover(String startTag, String endTag) {
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
