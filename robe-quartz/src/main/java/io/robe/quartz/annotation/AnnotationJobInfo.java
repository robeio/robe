package io.robe.quartz.annotation;


import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.TriggerInfo;
import io.robe.quartz.job.schedule.QJob;
import io.robe.quartz.job.schedule.QTrigger;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.List;

public class AnnotationJobInfo implements JobInfo {
    private String name;
    private String description;
    private List<TriggerInfo> triggers;
    private Class<? extends Job> jobClass;

    public AnnotationJobInfo(QJob ann, Class<? extends Job> jobClass) {
        name = ann.name();
        description = ann.description();
        triggers = new ArrayList<>(ann.triggers().length);
        for (QTrigger tAnn : ann.triggers()) {
            triggers.add(new AnnotationTriggerInfo(tAnn));
        }
        this.jobClass = jobClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    @Override
    public Class<? extends Job> getJobClass() {
        return jobClass;
    }
}
