package io.robe.hibernate.entity;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BaseEntityTest {

    @Test
    public void getterSetter() {

        long lastUpdated = 0;
        String uuid = UUID.randomUUID().toString();

        BaseTestEntity entity = new BaseTestEntity();
        entity.setLastUpdated(0);
        entity.setOid(uuid);

        assertEquals(lastUpdated, entity.getLastUpdated(), 0);
        assertEquals(uuid, entity.getOid());
    }

    private class BaseTestEntity extends BaseEntity {

    }
}