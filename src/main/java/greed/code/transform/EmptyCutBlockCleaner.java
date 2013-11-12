package greed.code.transform;

import greed.code.CodeByLine;
import greed.code.ConfigurableCodeTransformer;

import java.util.ArrayList;

/**
 * Greed is good! Cheers!
 */
public class EmptyCutBlockCleaner implements ConfigurableCodeTransformer {

    private String startTag;
    private String endTag;

    public EmptyCutBlockCleaner(String startTag, String endTag) {
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
                buffer.add(line);
            }
            else if (line.trim().equals(endTag)) {
                buffer.add(line);
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

    @Override
    public String getId() {
        return "empty-block";
    }
}
