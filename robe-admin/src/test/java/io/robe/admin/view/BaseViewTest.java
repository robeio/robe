package io.robe.admin.view;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.robe.admin.RobeApplication;
import org.junit.Before;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasanmumin on 10/10/2016.
 */
public class BaseViewTest {

    private Configuration configuration;
    private Template template;
    private Map<String, Object> parameters = new HashMap<>();
    private Writer out = new StringWriter();

    protected void addParameter(String name, Object value) {
        this.parameters.put(name, value);
    }

    @Before
    public void before() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setClassForTemplateLoading(RobeApplication.class, "/");
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
    }

    protected Template loadTemplate(String template) throws IOException {
        this.template = configuration.getTemplate(template);
        return this.template;
    }

    protected String process() throws IOException, TemplateException {
        this.template.process(this.parameters, out);
        return out.toString();
    }
}
