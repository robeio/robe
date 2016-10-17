package io.robe.admin.hibernate.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by hasanmumin on 17/10/2016.
 */
public class RoleGroupTest {

    @Test
    public void roleGroup() {
        RoleGroup roleGroup = new RoleGroup();
        String oid = UUID.randomUUID().toString();
        roleGroup.setOid(oid);
        Assert.assertTrue(roleGroup.getOid().equals(oid));
        Assert.assertTrue(roleGroup.getId().equals(oid));
    }
}
