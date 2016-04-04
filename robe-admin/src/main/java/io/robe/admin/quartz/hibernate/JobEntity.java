package io.robe.admin.quartz.hibernate;

import io.robe.hibernate.entity.BaseEntity;
import org.quartz.Job;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class JobEntity extends BaseEntity {

    private String name;

    private Class<? extends Job> jobClass;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }
}
