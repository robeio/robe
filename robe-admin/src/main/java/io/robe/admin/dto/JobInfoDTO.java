package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class JobInfoDTO extends HJobInfo implements JobInfo {

    @Transient
    private List<TriggerInfo> triggers = new ArrayList<>();

    @Transient
    private Status status;

    public JobInfoDTO() {

    }

    public JobInfoDTO(HJobInfo entity) {
        setJobClass(entity.getJobClass());
        setName(entity.getName());
        setGroup(entity.getGroup());
        setDescription(entity.getDescription());
        setLastUpdated(entity.getLastUpdated());
        setOid(entity.getOid());
        setProvider(entity.getProvider());
    }


    @Override
    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerInfo> triggers) {
        this.triggers = triggers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACTIVE,
        PAUSED,
        UNSCHEDULED
    }
}
