package io.robe.quartz.annotations;

import io.robe.quartz.CronProvider;

import javax.persistence.TemporalType;
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

    Class<? extends CronProvider> provider();

    String description() default "Quartz Job";

    boolean autoStart() default true;

    TemporalType date();

}
