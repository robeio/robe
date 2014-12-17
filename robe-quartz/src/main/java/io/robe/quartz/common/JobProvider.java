package io.robe.quartz.common;

import org.quartz.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public abstract class JobProvider {

    public abstract JobInfo getJob(Class<? extends Job> clazz);


    /**
     * Creates an identical JobDetail instance from the given parameters.
     *
     * @param info job annotation
     * @return
     */
    public static JobDetail convert2JobDetail(JobInfo info) {
        JobKey jobKey = JobKey.jobKey(info.getName());
        JobDetail jobDetail = newJob(info.getJobClass()).
                withIdentity(jobKey).
                build();
        return jobDetail;
    }

    public List<Trigger> convert2Triggers(JobInfo dto) {
        List<Trigger> triggers = new LinkedList<>();
        for (TriggerInfo trigDto : dto.getTriggers()) {
            triggers.add(convert2Trigger(trigDto));
        }
        return triggers;
    }

    /**
     * Creates an identical Trigger instance from the given annotation.
     *
     * @param dto
     * @return
     */
    public static Trigger convert2Trigger(TriggerInfo dto) {
        TriggerBuilder<Trigger> builder = newTrigger();
        builder.withIdentity(dto.getName(), dto.getGroup());

        switch (dto.getType()) {
            case CRON:
                setStartEndTime(dto, builder);

                if (!dto.getCron().isEmpty())
                    builder.withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron()));
                break;
            case SIMPLE:
                setStartEndTime(dto, builder);
                setCountIntervalValues(dto, builder);
                break;
        }

        return builder.build();
    }

    /**
     * Helps to set count and intervals
     *
     * @param dto
     * @param builder
     */
    private static void setCountIntervalValues(TriggerInfo dto, TriggerBuilder<org.quartz.Trigger> builder) {
        SimpleScheduleBuilder builderSc = SimpleScheduleBuilder.simpleSchedule();
        if (dto.getRepeatCount() != 0)
            builderSc.withRepeatCount(dto.getRepeatCount());

        if (dto.getRepeatInterval() > 0)
            builderSc.withIntervalInMilliseconds(dto.getRepeatInterval());
        builder.withSchedule(builderSc);
    }


    /**
     * Helps to set start and end times
     *
     * @param dto
     * @param builder
     */
    private static void setStartEndTime(TriggerInfo dto, TriggerBuilder<org.quartz.Trigger> builder) {
        if (dto.getStartTime() > -1)
            builder.startAt(new Date(dto.getStartTime()));
        else
            builder.startNow();

        if (dto.getEndTime() > -1)
            builder.endAt(new Date(dto.getEndTime()));
    }
}
