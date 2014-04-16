package io.robe.quartz.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class for cron operations operations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

    String cron();

    String description() default "Quartz Job";

    /**
     * Start time as string. Format: "dd.MM.yyyy hh:mm:ss"
     *
     * @return
     */
    String startTime() default "";

    boolean autoStart() default true;

}
