package io.robe.admin.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sinanselimoglu on 19/02/14.
 */
@Entity
@Table
public class QuartzJob extends BaseEntity {
    private String cronExpression;
    private String schedulerName;
    private String jobClassName;

    @JsonIgnore
    private String fireTime;


    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

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

    public String getFireTime() {
        return fireTime;
    }

    public void setFireTime(String fireTime) {
        this.fireTime = fireTime;
    }

}
