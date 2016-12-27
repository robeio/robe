package io.robe.quartz;

import io.robe.quartz.info.TriggerInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class for cron operations operations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobeTrigger {
    String name();

    String group();

    long startTime() default -1;

    long endTime() default -1;

    int repeatCount() default 0;

    long repeatInterval() default 0;

    String cron() default "";

    TriggerInfo.Type type();


}
