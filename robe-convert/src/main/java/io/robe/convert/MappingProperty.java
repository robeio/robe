package io.robe.convert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MappingProperty {

    int order() default 0;

    String name() default "";

    boolean unique() default false;

    boolean optional() default true;

    int length() default -1;

    int max() default -1;

    int min() default -1;

    boolean hidden() default false;

    int columnWidth() default 0;
}
