package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.job.SampleJob;
import io.robe.test.request.TestRequest;
import io.robe.test.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class QuartzHibernateJobInfoResourceTest extends BaseResourceTest<HibernateJobInfo> {
    @Override
    public String getPath() {
        return "quartzjobs";
    }

    @Override
    public Class<HibernateJobInfo> getClazz() {
        return HibernateJobInfo.class;
    }

    @Override
    public void assertEquals(HibernateJobInfo model, HibernateJobInfo response) {
        Assert.assertEquals(model.getDescription(), response.getDescription());
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getJobClass(), response.getJobClass());
    }

    @Override
    public void assertEquals(HibernateJobInfo mergeInstance, HibernateJobInfo original, HibernateJobInfo response) {
        Assert.assertEquals(mergeInstance.getDescription(), response.getDescription());
        Assert.assertEquals(original.getName(), response.getName());
        Assert.assertEquals(original.getJobClass(), response.getJobClass());
    }

    @Override
    public HibernateJobInfo instance() {
        HibernateJobInfo instance = new HibernateJobInfo();
        instance.setDescription("Description");
        instance.setName("NAME");
        instance.setJobClass(SampleJob.class);

        return instance;
    }

    @Override
    public HibernateJobInfo update(HibernateJobInfo response) {
        response.setDescription("Description updated");
        return response;
    }

    @Override
    public HibernateJobInfo mergeInstance() {
        HibernateJobInfo instance = new HibernateJobInfo();
        instance.setDescription("Description updated again");
        return instance;
    }

    @Test
    public void getJobTriggers() throws Exception {

        HibernateJobInfo entity = super.createFrom();
        TestRequest request = getRequestBuilder().endpoint(entity.getOid() + "/triggers").build();
        try {
            TestResponse response = client.get(request);// TODO add trigger and handle response.
        } catch (Exception e) {
            // TODO ignored Exception
        }
        super.deleteFrom(entity);
    }
}
