package greed.conf.schema;

import greed.conf.meta.ConfigObjectClass;
import greed.conf.meta.MapParam;
import greed.conf.meta.Required;

import java.util.HashMap;

/**
 * Greed is good! Cheers!
 */
@ConfigObjectClass
public class LanguageConfig {
    @Required
    private String cutBegin;

    @Required
    private String cutEnd;

    @Required
    private String[] templates;

    @Required
    @MapParam(value = TemplateConfig.class)
    private HashMap<String, TemplateConfig> templateDef;

    public String getCutBegin() {
        return cutBegin;
    }

    public void setCutBegin(String cutBegin) {
        this.cutBegin = cutBegin;
    }

    public String getCutEnd() {
        return cutEnd;
    }

    public void setCutEnd(String cutEnd) {
        this.cutEnd = cutEnd;
    }

    public String[] getTemplates() {
        return templates;
    }

    public void setTemplates(String[] templates) {
        this.templates = templates;
    }

    public HashMap<String, TemplateConfig> getTemplateDef() {
        return templateDef;
    }

    public void setTemplateDef(HashMap<String, TemplateConfig> templateDef) {
        this.templateDef = templateDef;
    }
}
