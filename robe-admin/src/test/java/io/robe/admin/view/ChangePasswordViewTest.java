package io.robe.admin.view;

import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hasanmumin on 10/10/2016.
 */
public class ChangePasswordViewTest extends BaseViewTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangePasswordViewTest.class);

    @Test
    public void changePasswordView() throws IOException, TemplateException {

        String ticket = UUID.randomUUID().toString();
        String mail = "admin@robe.io";
        String url = "http://127.0.0.1:8080/robe/";
        ChangePasswordView view = new ChangePasswordView(ticket, mail, url);

        super.loadTemplate(view.getTemplateName());

        super.addParameter(view.getMail().getName(), view.getMail());
        super.addParameter(view.getTicket().getName(), view.getTicket());
        super.addParameter(view.getUrl().getName(), view.getUrl());

        String result = super.process();

        Assert.assertTrue(result.contains(url));
        Assert.assertTrue(result.contains(mail));
        Assert.assertTrue(result.contains(ticket));
    }
}
