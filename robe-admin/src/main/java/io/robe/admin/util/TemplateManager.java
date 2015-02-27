package io.robe.admin.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.robe.common.exception.RobeRuntimeException;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasanmumin on 27/02/15.
 */
public class TemplateManager {

    private static final String TEMPLATES_PATH = "/templates/";
    private static Configuration cfg = null;
    private Map<String, Object> parameter = new HashMap<String, Object>();
    private Template template;

    public TemplateManager() {
        if (cfg == null) {
            cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            cfg.setClassForTemplateLoading(this.getClass(), TEMPLATES_PATH);
        }
    }

    public TemplateManager(Template template) {
        this();
        this.template = template;
    }

    public TemplateManager(String templateName, String templateBody) {
        this();
        try {
            template = new Template(templateName, templateBody, cfg);
        } catch (IOException e) {
            throw new RobeRuntimeException("Template Exception", e);
        }
    }

    public TemplateManager(String templateName) {
        this();
        try {
            template = cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new RobeRuntimeException("Template Exception", e);
        }
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
    }

    public void process(Writer out) {
        if (template == null) {
            throw new RobeRuntimeException("Template Exception", "Template can not be null");
        }

        try {
            template.process(parameter, out);
        } catch (TemplateException | IOException e) {
            throw new RobeRuntimeException("Template Exception", e);
        }
    }
}

