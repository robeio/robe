package io.robe.quartz.job.schedule;

import io.robe.quartz.common.JobProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class for cron jobs which will be scheduled by a provider class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledBy {

    Class<? extends JobProvider> provider();

    String description() default "Quartz Job";

}
