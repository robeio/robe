package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.entity.BaseEntity;
import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.TriggerInfo;
import org.quartz.Job;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class JobEntity extends BaseEntity implements JobInfo {

    private String name;

    private Class<? extends Job> jobClass;

    private String description;

    @Transient
    private List<TriggerInfo> triggers = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerInfo> triggers) {
        this.triggers = triggers;
    }

    @Override
    public Class<? extends Job> getJobClass() {

        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }
}
