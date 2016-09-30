package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class LanguageTest {
    Language entity = new Language();

    @Test
    public void getName() throws Exception {
        entity.setName("Name");
        assertEquals("Name", entity.getName());
    }

    @Test
    public void getCode() throws Exception {
        entity.setCode(Language.Type.TR);
        assertEquals(Language.Type.TR, entity.getCode());

        entity.setCode(Language.Type.EN);
        assertEquals(Language.Type.EN, entity.getCode());
    }


}
