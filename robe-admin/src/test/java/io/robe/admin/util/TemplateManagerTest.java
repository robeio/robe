package io.robe.admin.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hasanmumin on 11/10/2016.
 */
public class TemplateManagerTest {


    @Test
    public void templateShouldThrowRobeRuntimeException1() {

        try {
            new TemplateManager("NotExistTemplate.ftl");
            Assert.fail("Should throw Exception ");
        } catch (Exception e) {
            Assert.assertTrue("NotExistTemplate throw Exception", true);
        }
    }

    @Test
    public void templateShouldThrowRobeRuntimeException2() {

        try {
            new TemplateManager().process(new StringWriter());
            Assert.fail("Should throw Exception ");
        } catch (Exception e) {
            Assert.assertTrue("Throw Exception", true);
        }
    }

    @Test
    public void templateShouldThrowRobeRuntimeException3() {

        try {
            new TemplateManager("TEST", "<p>${\\undefined}</p>");
            Assert.fail("Should throw Exception ");
        } catch (Exception e) {
            Assert.assertTrue("Throw Exception", true);
        }
    }

    @Test
    public void templateShouldThrowRobeRuntimeException4() {

        try {
            new TemplateManager("RegisterMail.ftl").process(new StringWriter());
            Assert.fail("Should throw Exception ");
        } catch (Exception e) {
            Assert.assertTrue("Throw Exception", true);
        }
    }

    @Test
    public void templateManagerWithTemplateName() {
        TemplateManager manager = new TemplateManager("RegisterMail.ftl");

        Map<String, Object> parameters = new HashMap<>();

        String uuid = UUID.randomUUID().toString();
        String ticketUrl = "http:127.0.0.1:8080/robe/register/" + uuid;
        parameters.put("ticketUrl", ticketUrl);

        manager.setParameter(parameters);

        Assert.assertTrue(manager.getParameter().get("ticketUrl").equals(ticketUrl));

        Writer out = new StringWriter();
        manager.process(out);

        String result = out.toString();

        Assert.assertTrue(result.contains(ticketUrl));
    }


    @Test
    public void templateManagerWithNameAndBody() {

        String content = "<p>${item}</p>";
        TemplateManager manager = new TemplateManager("test", content);

        Map<String, Object> parameters = new HashMap<>();

        String uuid = UUID.randomUUID().toString();
        parameters.put("item", uuid);

        manager.setParameter(parameters);
        Assert.assertTrue(manager.getParameter().get("item").equals(uuid));


        Writer out = new StringWriter();
        manager.process(out);

        String result = out.toString();

        Assert.assertTrue(result.equals("<p>" + uuid + "</p>"));
    }

    @Test
    public void templateManagerWithTemplate() throws IOException {

        String content = "<p>${item}</p>";

        Template template = new Template("name", content, new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        TemplateManager manager = new TemplateManager(template);


        Map<String, Object> parameters = new HashMap<>();

        String uuid = UUID.randomUUID().toString();
        parameters.put("item", uuid);

        manager.setParameter(parameters);
        Assert.assertTrue(manager.getParameter().get("item").equals(uuid));

        Writer out = new StringWriter();
        manager.process(out);

        String result = out.toString();

        Assert.assertTrue(result.equals("<p>" + uuid + "</p>"));

    }
}
