package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class SystemParameterTest {
    SystemParameter entity = new SystemParameter();

    @Test
    public void getKey() throws Exception {
        entity.setKey("Key");
        assertEquals("Key", entity.getKey());
    }

    @Test
    public void getValue() throws Exception {
        entity.setValue("Value");
        assertEquals("Value", entity.getValue());
    }

    @Test
    public void tesToString() throws Exception {
        SystemParameter parameter = new SystemParameter();
        parameter.setKey("Key");
        parameter.setValue("Value");

        entity.setKey("Key");
        entity.setValue("Value");

        assertEquals(parameter.toString(), entity.toString());
    }
}
