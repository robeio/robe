package io.robe.quartz.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * Annotation class for cron operations operations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

    public static String DYNAMIC_GROUP="DynamicCronJob";
    public static String STATIC_GROUP="StaticCronJob";
    enum Manager{
        REMOTE_EXTERNAL,
        RUN_TIME
    }
    String cron();
    Manager manager() default Manager.RUN_TIME;
    String description() default "Quartz Job";
    long startTime() default -1;

}
