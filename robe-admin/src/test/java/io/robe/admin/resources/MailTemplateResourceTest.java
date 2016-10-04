package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.MailTemplate;
import org.junit.Assert;

/**
 * Created by hasanmumin on 04/10/16.
 */
public class MailTemplateResourceTest extends BaseResourceTest<MailTemplate> {
    @Override
    public String getPath() {
        return "mailtemplates";
    }

    @Override
    public Class<MailTemplate> getClazz() {
        return MailTemplate.class;
    }

    @Override
    public void assertEquals(MailTemplate model, MailTemplate response) {
        Assert.assertEquals(model.getCode(), response.getCode());
        Assert.assertEquals(model.getLanguage(), response.getLanguage());
        Assert.assertEquals(new String(model.getTemplate()), new String(response.getTemplate()));
    }

    @Override
    public MailTemplate instance() {

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setCode("CODE");
        mailTemplate.setTemplate("Mail Template Content".toCharArray());
        mailTemplate.setLanguage(MailTemplate.Type.EN);
        return mailTemplate;
    }

    @Override
    public MailTemplate update(MailTemplate response) {
        response.setLanguage(MailTemplate.Type.TR);
        response.setTemplate("new template".toCharArray());
        return response;
    }
}
