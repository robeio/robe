package io.robe.quartz.annotation;


import com.google.common.collect.Lists;
import io.robe.quartz.CronProvider;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.QuartzJob;
import io.robe.quartz.QuartzTrigger;
import io.robe.quartz.annotations.Scheduled;
import org.quartz.Job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ByAnnotation implements CronProvider {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");


    public QuartzJob getQuartzJob(Class<? extends Job> clazz) {
        // Create Job Definition by annotation.
        QuartzJobPojo job = new QuartzJobPojo();
        Scheduled annotation = clazz.getAnnotation(Scheduled.class);
        if (annotation == null) {
            throw new NullPointerException("Job class must have Scheduled annotation.");
        }

        job.setOid(clazz.getSimpleName());
        job.setClazz(clazz);
        job.setDescription(annotation.description());
        job.setSchedulerName(QuartzBundle.STATIC_GROUP);

        // Create Trigger Definition by annotation.
        QuartzTriggerPojo trigger = new QuartzTriggerPojo();
        trigger.setOid(clazz.getSimpleName() + "-Trigger");
        trigger.setActive(annotation.autoStart());
        trigger.setCronExpression(annotation.cron());

        try {
            trigger.setFireTime(FORMAT.parse(annotation.startTime()).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        trigger.setJobId(job.getOid());
        List<QuartzTrigger> triggers = Lists.newLinkedList();
        triggers.add((QuartzTrigger) trigger);
        job.setTriggers(triggers);
        return job;
    }
}
