package io.robe.quartz.job.schedule;

import io.robe.quartz.common.TriggerInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class for cron operations operations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QTrigger {
    public String name();

    public String group();

    public long startTime() default -1;

    public long endTime() default -1;

    public int repeatCount() default 0;

    public long repeatInterval() default 0;

    public String cron() default "";

    public boolean autoStart() default true;

    public TriggerInfo.Type type();


}
