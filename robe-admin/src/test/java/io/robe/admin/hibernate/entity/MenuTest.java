package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class MenuTest {
    Menu entity = new Menu();

    @Test
    public void getText() throws Exception {
        entity.setText("Text");
        assertEquals("Text", entity.getText());
    }

    @Test
    public void getPath() throws Exception {
        entity.setPath("Path");
        assertEquals("Path", entity.getPath());
    }

    @Test
    public void getIndex() throws Exception {
        entity.setIndex(3);
        assertEquals(3, entity.getIndex());
    }

    @Test
    public void getParentOid() throws Exception {
        entity.setParentOid("12345678901234567890123456789012");
        assertEquals("12345678901234567890123456789012", entity.getParentOid());
    }

    @Test
    public void getModule() throws Exception {
        entity.setModule("Module");
        assertEquals("Module", entity.getModule());
    }

    @Test
    public void getIcon() throws Exception {
        entity.setIcon("Icon");
        assertEquals("Icon", entity.getIcon());
    }

}
