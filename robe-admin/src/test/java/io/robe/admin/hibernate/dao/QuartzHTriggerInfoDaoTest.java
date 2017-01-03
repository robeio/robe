package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HTriggerInfo;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class QuartzHTriggerInfoDaoTest extends BaseDaoTest<HTriggerInfo, TriggerDao> {
    @Override
    public HTriggerInfo instance() {
        HTriggerInfo entity = new HTriggerInfo();
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
    public HTriggerInfo update(HTriggerInfo model) {
        model.setName("NAME_1");
        return model;
    }

    @Test
    public void findByJobOid() {
        super.createFrom();

        List<HTriggerInfo> hTriggerInfoEntities = dao.findByJobOid("1");
        Assert.assertTrue(hTriggerInfoEntities.size() == 1);
        super.deleteFrom(hTriggerInfoEntities.get(0));
    }
}
