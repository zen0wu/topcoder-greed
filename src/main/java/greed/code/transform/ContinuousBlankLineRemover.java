package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

/**
 * @author WuCY
 */
public class ContinuousBlankLineRemover implements CodeTransformer {
    @Override
    public CodeByLine transform(CodeByLine input) {
        CodeByLine res = new CodeByLine();
        int lastLen = 0;
        for (String line: input.getLines()) {
            if (line.trim().length() == 0 && lastLen == 0) {
                // skip this line
            }
            else res.getLines().add(line);
            lastLen = line.trim().length();
        }
        return res;
    }
}
