package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.entity.BaseEntity;
import io.robe.quartz.info.TriggerInfo;

import javax.persistence.*;

@Entity
@Table
public class TriggerEntity extends BaseEntity implements TriggerInfo {

    private String name;
    @Column(name = "triggerGroup")
    private String group;
    private long startTime = -1;
    private long endTime = -1;
    private int repeatCount = 0;
    private long repeatInterval = 0;
    private String cron;
    @Column(name = "triggerType")
    @Enumerated(EnumType.STRING)
    private Type type;
    private boolean active;
    @Column(length = 32)
    private String jobOid;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    @Override
    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    @Override
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getJobOid() {
        return jobOid;
    }

    public void setJobOid(String jobOid) {
        this.jobOid = jobOid;
    }
}
