package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class RoleTest {
    Role entity = new Role();

    @Test
    public void getName() throws Exception {
        entity.setName("Name");
        assertEquals("Name", entity.getName());
    }

    @Test
    public void getCode() throws Exception {
        entity.setCode("Code");
        assertEquals("Code", entity.getCode());
    }

    @Test
    public void getId() throws Exception {
        entity.setOid("id");
        assertEquals("id", entity.getId());
    }

}
