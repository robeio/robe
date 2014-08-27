package io.robe.quartz.annotation;

import io.robe.quartz.QuartzJob;
import io.robe.quartz.QuartzTrigger;
import org.quartz.Job;

import java.util.List;

public class QuartzJobPojo implements QuartzJob {
    private String oid;
    private String schedulerName;
    private Class<? extends Job> clazz;
    private String description;
    private List<QuartzTrigger> triggers;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public Class<? extends Job> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Job> clazz) {
        this.clazz = clazz;
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