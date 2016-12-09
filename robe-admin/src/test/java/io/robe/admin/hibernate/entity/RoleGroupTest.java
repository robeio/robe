package io.robe.admin.hibernate.entity;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class RoleGroupTest {
    RoleGroup entity = new RoleGroup();

    @Test
    public void roleGroup() {
        RoleGroup roleGroup = new RoleGroup();
        String oid = UUID.randomUUID().toString();
        roleGroup.setOid(oid);
        Assert.assertTrue(roleGroup.getOid().equals(oid));
        Assert.assertTrue(roleGroup.getId().equals(oid));
    }

    @Test
    public void getRoleOid() throws Exception {
        entity.setRoleOid("123451234512345");
        assertEquals("123451234512345", entity.getRoleOid());
    }

    @Test
    public void getGroupOid() throws Exception {
        entity.setGroupOid("123451234512345");
        assertEquals("123451234512345", entity.getGroupOid());
    }

    @Test
    public void getId() throws Exception {
        entity.setOid("123451234512345");
        assertEquals("123451234512345", entity.getId());
    }

    @Test
    public void getRoleId() throws Exception {
        entity.setRoleOid("123451234512345");
        assertEquals("123451234512345", entity.getRoleId());
    }

}
