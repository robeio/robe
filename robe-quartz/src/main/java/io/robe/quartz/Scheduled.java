package io.robe.quartz;

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

    enum Manager{
        DB,
        ANNOTATION
    }
    String cron();
    Manager manager() default Manager.ANNOTATION;


}
