package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class QuartzHibernateTriggerInfoDaoTest extends BaseDaoTest<HibernateTriggerInfo, QuartzTriggerDao> {
    @Override
    public HibernateTriggerInfo instance() {
        HibernateTriggerInfo entity = new HibernateTriggerInfo();
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
    public HibernateTriggerInfo update(HibernateTriggerInfo model) {
        model.setName("NAME_1");
        return model;
    }

    @Test
    public void findByJobOid() {
        super.createFrom();

        List<HibernateTriggerInfo> hibernateTriggerInfoEntities = dao.findByJobOid("1");
        Assert.assertTrue(hibernateTriggerInfoEntities.size() == 1);
        super.deleteFrom(hibernateTriggerInfoEntities.get(0));
    }
}
