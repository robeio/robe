package io.robe.admin.view;

import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hasanmumin on 10/10/2016.
 */
public class RegisterViewTest extends BaseViewTest {

    @Test
    public void RegisterView() throws IOException, TemplateException {


        String ticket = UUID.randomUUID().toString();
        String mail = "admin@robe.io";
        String url = "http://127.0.0.1:8080/robe/";

        RegisterView registerView = new RegisterView(mail, ticket, url);

        super.loadTemplate(registerView.getTemplateName());

        super.addParameter(registerView.getMail().getName(), registerView.getMail());
        super.addParameter(registerView.getTicket().getName(), registerView.getTicket());
        super.addParameter(registerView.getUrl().getName(), registerView.getUrl());

        String result = super.process();

        Assert.assertTrue(result.contains(ticket));
        Assert.assertTrue(result.contains(mail));
        Assert.assertTrue(result.contains(url));
    }
}
