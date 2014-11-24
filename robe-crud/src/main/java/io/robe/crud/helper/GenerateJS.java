package io.robe.crud.helper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenerateJS {

    private Configuration cfg = null;

    public GenerateJS() {
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
    }

    public String createHtml(String entity) throws IOException, TemplateException {

        Template template = cfg.getTemplate("html.ftl");
        Map<String, String> data = new HashMap<String, String>();
        data.put("entity", entity);

        Writer out = new StringWriter();

        template.process(data, out);

        out.flush();


        return out.toString();
    }


    public String createView(String entity, List<Model> fields) throws IOException, TemplateException {

        Template template = cfg.getTemplate("view.ftl");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("entity", entity);
        data.put("fields", fields);
        Writer out = new StringWriter();
        template.process(data, out);

        out.flush();

        return out.toString();

    }


    public String createModel(String entity, List<Model> fields) throws IOException, TemplateException {

        Template template = cfg.getTemplate("model.ftl");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("entity", entity + "Model");
        data.put("fields", fields);

        Writer out = new StringWriter();

        template.process(data, out);

        out.flush();


        return out.toString();

    }

    public String createDataSource(String entity) throws IOException, TemplateException {

        Template template = cfg.getTemplate("datasource.ftl");
        Map<String, String> data = new HashMap<String, String>();
        data.put("entity", entity);

        Writer out = new StringWriter();

        template.process(data, out);

        out.flush();


        return out.toString();


    }
}
