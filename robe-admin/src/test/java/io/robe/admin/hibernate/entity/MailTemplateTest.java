package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class MailTemplateTest {
    MailTemplate entity = new MailTemplate();

    @Test
    public void getCode() throws Exception {
        entity.setCode("Code");
        assertEquals("Code", entity.getCode());
    }

    @Test
    public void getLanguage() throws Exception {
        entity.setLanguage(MailTemplate.Type.TR);
        assertEquals(MailTemplate.Type.TR, entity.getLanguage());

        entity.setLanguage(MailTemplate.Type.EN);
        assertEquals(MailTemplate.Type.EN, entity.getLanguage());
    }

    @Test
    public void getTemplate() throws Exception {
        entity.setTemplate("Template".toCharArray());
        assertArrayEquals("Template".toCharArray(), entity.getTemplate());
    }


}
