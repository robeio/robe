package io.robe.quartz.job.hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.robe.hibernate.entity.BaseEntity;
import io.robe.quartz.job.QuartzJob;
import io.robe.quartz.job.QuartzTrigger;
import org.quartz.Job;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class JobEntity extends BaseEntity implements QuartzJob {
    private String schedulerName;
    private String jobClassName;
    private String description;

    @Transient
    private Class<? extends Job> clazz;

    @JsonIgnore
    @JsonManagedReference("trigger")
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = TriggerEntity.class, orphanRemoval = true)
    private List<QuartzTrigger> triggers = new ArrayList<>();

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuartzTrigger> getTriggers() {
        return triggers;
    }


    public void setTriggers(List<QuartzTrigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public Class<? extends Job> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Job> clazz) {
        this.clazz = clazz;
    }
}
