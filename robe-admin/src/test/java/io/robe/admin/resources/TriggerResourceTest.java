package io.robe.admin.resources;

import io.robe.admin.quartz.hibernate.TriggerEntity;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.util.Date;

/**
 * Created by hasanmumin on 13/10/2016.
 */
public class TriggerResourceTest extends BaseResourceTest<TriggerEntity> {
    @Override
    public String getPath() {
        return "triggers";
    }

    @Override
    public Class<TriggerEntity> getClazz() {
        return TriggerEntity.class;
    }

    @Override
    public void assertEquals(TriggerEntity model, TriggerEntity response) {
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getJobOid(), response.getJobOid());
        Assert.assertEquals(model.getCron(), response.getCron());
        Assert.assertEquals(model.getGroup(), response.getGroup());
    }

    @Override
    public void assertEquals(TriggerEntity mergeInstance, TriggerEntity original, TriggerEntity response) {
        Assert.assertEquals(mergeInstance.getName(), response.getName());
        Assert.assertEquals(original.getJobOid(), response.getJobOid());
        Assert.assertEquals(original.getCron(), response.getCron());
        Assert.assertEquals(original.getGroup(), response.getGroup());
    }

    @Override
    public TriggerEntity instance() {
        TriggerEntity entity = new TriggerEntity();
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
    public TriggerEntity update(TriggerEntity response) {
        response.setName("NAME-1");
        return response;
    }

    @Override
    public TriggerEntity mergeInstance() {
        TriggerEntity entity = new TriggerEntity();
        entity.setName("NAME-2");
        return entity;
    }
}
