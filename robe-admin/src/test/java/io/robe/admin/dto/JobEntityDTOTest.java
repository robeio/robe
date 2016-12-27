package io.robe.admin.dto;

import io.robe.admin.job.SampleJob;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.quartz.info.TriggerInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class JobEntityDTOTest {
    JobEntity entity;
    ArrayList<TriggerInfo> triggerInfos;
    JobEntityDTO dto;

    @Before
    public void setUp() throws Exception {
        JobEntity entity = new JobEntity();
        entity.setName("Entity");
        entity.setDescription("Description");
        entity.setJobClass(entity.getJobClass());

        triggerInfos = new ArrayList<>(2);

        dto = new JobEntityDTO(entity);

    }

    @Test
    public void getTriggers() throws Exception {
        dto.setTriggers(triggerInfos);
        assertEquals(triggerInfos, dto.getTriggers());
    }

    @Test
    public void constructor() throws Exception {
        JobEntityDTO entityDTO = new JobEntityDTO();
        entityDTO.setName("Entity");
        assertEquals("Entity", entityDTO.getName());

    }
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
