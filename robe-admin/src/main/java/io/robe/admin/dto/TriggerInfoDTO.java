package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.Transient;

public class TriggerInfoDTO extends HTriggerInfo implements TriggerInfo {
    @Transient
    private JobInfoDTO.Status status;

    public TriggerInfoDTO() {

    }

    public TriggerInfoDTO(HTriggerInfo entity) {
        setOid(entity.getOid());
        setLastUpdated(entity.getLastUpdated());
        setGroup(entity.getGroup());
        setName(entity.getName());
        setType(entity.getType());
        setJobOid(entity.getJobOid());
        setCron(entity.getCron());
        setStartTime(entity.getStartTime());
        setEndTime(entity.getEndTime());
        setActive(entity.isActive());
        setRepeatCount(entity.getRepeatCount());
        setRepeatInterval(entity.getRepeatInterval());
    }

    public JobInfoDTO.Status getStatus() {
        return status;
    }

    public void setStatus(JobInfoDTO.Status status) {
        this.status = status;
    }
}
