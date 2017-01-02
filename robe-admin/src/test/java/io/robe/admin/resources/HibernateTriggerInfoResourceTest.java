package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.util.Date;

/**
 * Created by hasanmumin on 13/10/2016.
 */
public class HibernateTriggerInfoResourceTest extends BaseResourceTest<HibernateTriggerInfo> {
    @Override
    public String getPath() {
        return "triggers";
    }

    @Override
    public Class<HibernateTriggerInfo> getClazz() {
        return HibernateTriggerInfo.class;
    }

    @Override
    public void assertEquals(HibernateTriggerInfo model, HibernateTriggerInfo response) {
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getJobOid(), response.getJobOid());
        Assert.assertEquals(model.getCron(), response.getCron());
        Assert.assertEquals(model.getGroup(), response.getGroup());
    }

    @Override
    public void assertEquals(HibernateTriggerInfo mergeInstance, HibernateTriggerInfo original, HibernateTriggerInfo response) {
        Assert.assertEquals(mergeInstance.getName(), response.getName());
        Assert.assertEquals(original.getJobOid(), response.getJobOid());
        Assert.assertEquals(original.getCron(), response.getCron());
        Assert.assertEquals(original.getGroup(), response.getGroup());
    }

    @Override
    public HibernateTriggerInfo instance() {
        HibernateTriggerInfo entity = new HibernateTriggerInfo();
        entity.setName("NAME");
        entity.setActive(true);
        entity.setJobOid("1");
        entity.setCron("* * * * *");
        entity.setStartTime(new Date().getTime());
        entity.setEndTime(new DateTime().plusDays(1).toDate().getTime());
        entity.setGroup("GROUP");
        return entity;
    }

    @Override
    public HibernateTriggerInfo update(HibernateTriggerInfo response) {
        response.setName("NAME-1");
        return response;
    }

    @Override
    public HibernateTriggerInfo mergeInstance() {
        HibernateTriggerInfo entity = new HibernateTriggerInfo();
        entity.setName("NAME-2");
        return entity;
    }
}
