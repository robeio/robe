package io.robe.admin.dto;

import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.quartz.common.TriggerInfo;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.ArrayList;

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
}
