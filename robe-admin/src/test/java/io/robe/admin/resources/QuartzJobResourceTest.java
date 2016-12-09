package io.robe.admin.resources;

import io.robe.admin.job.SampleJob;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.test.request.TestRequest;
import io.robe.test.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class QuartzJobResourceTest extends BaseResourceTest<JobEntity> {
    @Override
    public String getPath() {
        return "quartzjobs";
    }

    @Override
    public Class<JobEntity> getClazz() {
        return JobEntity.class;
    }

    @Override
    public void assertEquals(JobEntity model, JobEntity response) {
        Assert.assertEquals(model.getDescription(), response.getDescription());
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getJobClass(), response.getJobClass());
    }

    @Override
    public void assertEquals(JobEntity mergeInstance, JobEntity original, JobEntity response) {
        Assert.assertEquals(mergeInstance.getDescription(), response.getDescription());
        Assert.assertEquals(original.getName(), response.getName());
        Assert.assertEquals(original.getJobClass(), response.getJobClass());
    }

    @Override
    public JobEntity instance() {
        JobEntity instance = new JobEntity();
        instance.setDescription("Description");
        instance.setName("NAME");
        instance.setJobClass(SampleJob.class);

        return instance;
    }

    @Override
    public JobEntity update(JobEntity response) {
        response.setDescription("Description updated");
        return response;
    }

    @Override
    public JobEntity mergeInstance() {
        JobEntity instance = new JobEntity();
        instance.setDescription("Description updated again");
        return instance;
    }

    @Test
    public void getJobTriggers() throws Exception {

        JobEntity entity = super.createFrom();
        TestRequest request = getRequestBuilder().endpoint(entity.getOid() + "/triggers").build();
        try {
            TestResponse response = client.get(request);// TODO add trigger and handle response.
        } catch (Exception e) {
            // TODO ignored Exception
        }
        super.deleteFrom(entity);
    }
}
