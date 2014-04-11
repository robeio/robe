package io.robe.quartz;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table
public class QuartzJob extends BaseEntity {
    private String schedulerName;
    private String jobClassName;
    private String description;

    @OneToMany(mappedBy = "oid")
    private List<QuartzTrigger> triggers;

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
}
