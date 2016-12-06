package io.robe.hibernate;

import io.robe.hibernate.entity.BaseEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by adem on 19/10/2016.
 */
public class BaseEntityTest {

    private String oid = "oid";
    private long lastUpdated = new Date().getTime();

    class Entity extends BaseEntity {
        public Entity() {
            super(oid, lastUpdated);
        }
    }

    @Test
    public void constructor() {
        Entity entity = new Entity();
        Assert.assertEquals(oid, entity.getOid());
        Assert.assertEquals(lastUpdated, entity.getLastUpdated());
    }

    @Test
    public void updateLastUpdated() {
        Entity entity = new Entity();
        lastUpdated = new Date().getTime();
        entity.setLastUpdated(lastUpdated);
        Assert.assertEquals(oid, entity.getOid());
        Assert.assertEquals(lastUpdated, entity.getLastUpdated());
    }

}
