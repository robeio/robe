package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.admin.job.SampleJob;
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
public class JobInfoDTOTest {
    HJobInfo entity;
    ArrayList<TriggerInfo> triggerInfos;
    JobInfoDTO dto;

    @Before
    public void setUp() throws Exception {
        HJobInfo entity = new HJobInfo();
        entity.setName("Entity");
        entity.setDescription("Description");
        entity.setJobClass(entity.getJobClass());

        triggerInfos = new ArrayList<>(2);

        dto = new JobInfoDTO(entity);

    }

    @Test
    public void getTriggers() throws Exception {
        dto.setTriggers(triggerInfos);
        assertEquals(triggerInfos, dto.getTriggers());
    }

    @Test
    public void constructor() throws Exception {
        JobInfoDTO entityDTO = new JobInfoDTO();
        entityDTO.setName("Entity");
        assertEquals("Entity", entityDTO.getName());

    }
    @Test
    public void jobEntityDTO() {
        JobInfoDTO dto = new JobInfoDTO();
        Assert.assertTrue(dto.getTriggers().size() == 0);
        dto.setTriggers(Collections.singletonList(new HTriggerInfo()));
        Assert.assertTrue(dto.getTriggers().size() == 1);
    }

    @Test
    public void jobEntityDTOWithJobEntity() {
        HJobInfo hJobInfo = new HJobInfo();
        hJobInfo.setJobClass(SampleJob.class);
        hJobInfo.setName("Name");
        hJobInfo.setDescription("Description");
        JobInfoDTO dto = new JobInfoDTO(hJobInfo);

        Assert.assertEquals(hJobInfo.getDescription(), dto.getDescription());
        Assert.assertEquals(hJobInfo.getName(), dto.getName());
        Assert.assertEquals(hJobInfo.getJobClass(), dto.getJobClass());

        Assert.assertTrue(dto.getTriggers().size() == 0);
        dto.setTriggers(Collections.singletonList(new HTriggerInfo()));
        Assert.assertTrue(dto.getTriggers().size() == 1);

    }
}
