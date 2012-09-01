package greed.code;

import greed.model.Param;
import greed.model.ParamValue;

/**
 * Greed is good! Cheers!
 */
public interface LanguageTrait {
    /**
     * @return the comment prefix for this language
     */
    public String getCommentPrefix();

    /**
     * Parse an array of values from value string
     *
     * @param value value string
     * @param param element type
     * @return parsed param value
     */
    public ParamValue parseValue(String value, Param param);
}
