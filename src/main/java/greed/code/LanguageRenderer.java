package greed.code;

import com.floreysoft.jmte.NamedRenderer;
import greed.model.*;

import java.util.List;

/**
 * Greed is good! Cheers!
 */
public interface LanguageRenderer {
    public String renderPrimitive(Primitive primitive);

    public String renderType(Type type);

    public String renderParam(Param param);

    public String renderParamValue(ParamValue paramValue);

    public String renderParamList(Param[] params);

    public String renderZeroValue(Type type);

    public String renderMethod(Method method);

    public List<NamedRenderer> getOtherRenderers();
}
