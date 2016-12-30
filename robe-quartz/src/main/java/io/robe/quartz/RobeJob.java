package io.robe.quartz;

import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.info.annotation.AnnotationJobInfoProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface RobeJob {

    String name();

    String group() default "DEFAULT";

    String description();

    RobeTrigger[] triggers() default {};

    Class<? extends JobInfoProvider> provider() default AnnotationJobInfoProvider.class;


}
