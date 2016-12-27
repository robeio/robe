package io.robe.admin.dto;

import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasanmumin on 04/04/16.
 */
public class JobEntityDTO extends JobEntity implements JobInfo {

    @Transient
    private List<TriggerInfo> triggers = new ArrayList<>();

    public JobEntityDTO() {

    }

    public JobEntityDTO(JobEntity entity) {
        setJobClass(entity.getJobClass());
        setName(entity.getName());
        setDescription(entity.getDescription());
        setLastUpdated(entity.getLastUpdated());
        setOid(entity.getOid());
    }

    @Override
    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerInfo> triggers) {
        this.triggers = triggers;
    }
}
