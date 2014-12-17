package io.robe.admin.quartz.hibernate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.robe.hibernate.entity.BaseEntity;
import io.robe.quartz.common.TriggerInfo;

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
    private String cron = "";
    private Type type;
    private boolean autostart;


    @JsonBackReference("job")
    @ManyToOne(cascade = CascadeType.REFRESH, targetEntity = JobEntity.class)
    @JoinColumn(name = "PARENTOID")
    private JobEntity job;


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

    public boolean isAutostart() {
        return autostart;
    }

    public void setAutostart(boolean autostart) {
        this.autostart = autostart;
    }

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
    }
}
