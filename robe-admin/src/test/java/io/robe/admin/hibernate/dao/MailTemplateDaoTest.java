package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.MailTemplate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class MailTemplateDaoTest extends BaseDaoTest<MailTemplate, MailTemplateDao> {
    @Override
    public MailTemplate instance() {

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setCode("CODE");
        mailTemplate.setLanguage(MailTemplate.Type.EN);
        mailTemplate.setTemplate("test content".toCharArray());

        return mailTemplate;
    }

    @Override
    public MailTemplate update(MailTemplate model) {
        model.setCode("CODE_EN");
        model.setLanguage(MailTemplate.Type.TR);
        model.setTemplate("test content updated".toCharArray());
        return model;
    }

    @Test
    public void findByCode() {
        super.createFrom();
        Optional<MailTemplate> mailTemplate = dao.findByCode("CODE");
        Assert.assertTrue(mailTemplate.isPresent());
        super.deleteFrom(mailTemplate.get());
    }
}
