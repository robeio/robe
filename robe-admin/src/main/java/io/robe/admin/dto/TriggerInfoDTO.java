package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.Transient;

public class TriggerInfoDTO extends HibernateTriggerInfo implements TriggerInfo {
    @Transient
    private JobInfoDTO.Status status;

    public TriggerInfoDTO() {

    }

    public TriggerInfoDTO(HibernateTriggerInfo entity) {
        setName(entity.getName());
        setGroup(entity.getGroup());
        setLastUpdated(entity.getLastUpdated());
        setOid(entity.getOid());
        setType(entity.getType());
        setJobOid(entity.getJobOid());
        setStartTime(entity.getStartTime());
        setEndTime(entity.getEndTime());
        setCron(entity.getCron());
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
