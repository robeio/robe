package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasanmumin on 04/04/16.
 */
public class HibernateJobInfoDTO extends HibernateJobInfo implements JobInfo {

    @Transient
    private List<TriggerInfo> triggers = new ArrayList<>();

    public HibernateJobInfoDTO() {

    }

    public HibernateJobInfoDTO(HibernateJobInfo entity) {
        setJobClass(entity.getJobClass());
        setName(entity.getName());
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
}
