package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.CodeTransformer;

import java.util.ArrayList;

/**
 * @author WuCY
 */
public class BlockCleaner implements CodeTransformer {

    private String startTag;
    private String endTag;

    public BlockCleaner(String startTag, String endTag) {
        this.startTag = startTag;
        this.endTag = endTag;
    }


    @Override
    public CodeByLine transform(CodeByLine input) {
        CodeByLine res = new CodeByLine();

        ArrayList<String> buffer = new ArrayList<String>();
        int totalLen = 0;
        boolean inBlock = false;
        for (String line: input.getLines()) {
            if (line.trim().equals(startTag)) {
                inBlock = true;
            }
            else if (line.trim().equals(endTag)) {
                if (totalLen > 0)
                    res.getLines().addAll(buffer);
                inBlock = false;
                buffer.clear();
                totalLen = 0;
            }
            else {
                if (inBlock) {
                    buffer.add(line);
                    totalLen += line.trim().length();
                }
                else
                    res.getLines().add(line);
            }
        }

        return res;
    }
}
