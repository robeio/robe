package io.robe.admin.hibernate.dao;

import io.robe.admin.quartz.hibernate.TriggerEntity;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class QuartzTriggerDaoTest extends BaseDaoTest<TriggerEntity, QuartzTriggerDao> {
    @Override
    public TriggerEntity instance() {
        TriggerEntity entity = new TriggerEntity();
        entity.setName("NAME");
        entity.setActive(true);
        entity.setCron("* * * * *");
        entity.setStartTime(new Date().getTime());
        entity.setEndTime(new DateTime().plusDays(1).toDate().getTime());
        entity.setGroup("GROUP");
        entity.setJobOid("1");
        return entity;
    }

    @Override
    public TriggerEntity update(TriggerEntity model) {
        model.setName("NAME_1");
        return model;
    }

    @Test
    public void findByJobOid() {
        super.createFrom();

        List<TriggerEntity> triggerEntities = dao.findByJobOid("1");
        Assert.assertTrue(triggerEntities.size() == 1);
        super.deleteFrom(triggerEntities.get(0));
    }
}
