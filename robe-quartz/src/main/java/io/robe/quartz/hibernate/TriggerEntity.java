package io.robe.quartz.hibernate;

import io.robe.hibernate.entity.BaseEntity;
import io.robe.quartz.QuartzTrigger;

import javax.persistence.*;

@Entity
@Table
public class TriggerEntity extends BaseEntity implements QuartzTrigger {

    private String cronExpression;

    @Transient
    private String jobId;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private boolean active;
    private long fireTime;

    @ManyToOne(targetEntity = JobEntity.class)
    private JobEntity job;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getFireTime() {
        return fireTime;
    }

    @Override
    public String getJobId() {
        return getJob().getOid();
    }

    public void setFireTime(long fireTime) {
        this.fireTime = fireTime;
    }

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
    }
}
