package io.robe.admin.dto;

import io.robe.admin.job.SampleJob;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class JobEntityDTOTest {

    @Test
    public void jobEntityDTO() {
        JobEntityDTO dto = new JobEntityDTO();
        Assert.assertTrue(dto.getTriggers().size() == 0);
        dto.setTriggers(Collections.singletonList(new TriggerEntity()));
        Assert.assertTrue(dto.getTriggers().size() == 1);
    }

    @Test
    public void jobEntityDTOWithJobEntity() {
        JobEntity jobEntity = new JobEntity();
        jobEntity.setJobClass(SampleJob.class);
        jobEntity.setName("Name");
        jobEntity.setDescription("Description");
        JobEntityDTO dto = new JobEntityDTO(jobEntity);

        Assert.assertEquals(jobEntity.getDescription(), dto.getDescription());
        Assert.assertEquals(jobEntity.getName(), dto.getName());
        Assert.assertEquals(jobEntity.getJobClass(), dto.getJobClass());

        Assert.assertTrue(dto.getTriggers().size() == 0);
        dto.setTriggers(Collections.singletonList(new TriggerEntity()));
        Assert.assertTrue(dto.getTriggers().size() == 1);

    }
}
