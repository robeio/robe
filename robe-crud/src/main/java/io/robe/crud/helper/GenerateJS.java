package io.robe.crud.helper;

import org.rythmengine.Rythm;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenerateJS {

    public String createHtml(String entity) throws IOException {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("entity", entity);
        Writer out = new StringWriter();
        Rythm.engine().render(out, "html.html", parameters);
        out.flush();

        return out.toString();
    }


    public String createView(String entity, List<Model> fields) throws IOException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("entity", entity);
        parameters.put("models", fields);
        Writer out = new StringWriter();
        Rythm.engine().render(out, "view.js", parameters);
        out.flush();
        return out.toString();

    }


    public String createModel(String entity, List<Model> fields) throws IOException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("entity", entity + "Model");
        parameters.put("models", fields);
        Writer out = new StringWriter();
        Rythm.engine().render(out, "model.js", parameters);
        out.flush();

        return out.toString();

    }

    public String createDataSource(String entity) throws IOException {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("entity", entity);
        Writer out = new StringWriter();
        Rythm.engine().render(out, "datasource.js", parameters);
        out.flush();

        return out.toString();
    }
}
