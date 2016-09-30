package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class PermissionTest {
    Permission entity = new Permission();

    @Test
    public void getRoleOid() throws Exception {
        entity.setRoleOid("1234561234567890123456789");
        assertEquals("1234561234567890123456789", entity.getRoleOid());
    }

    @Test
    public void getType() throws Exception {
        entity.setType(Permission.Type.MENU);
        assertEquals(Permission.Type.MENU, entity.getType());
    }

    @Test
    public void getRestrictedItemOid() throws Exception {
        entity.setRestrictedItemOid("1234561234567890123456789");
        assertEquals("1234561234567890123456789", entity.getRestrictedItemOid());
    }


    public void getpLevel() throws Exception {
        entity.setpLevel((short) 12);
        assertEquals((short) 12, entity.getpLevel());
    }
}
