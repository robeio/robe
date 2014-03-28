package io.robe.convert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kaanalkim on 20/03/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingProperty {
    int order() default 0;

    String name() default "";

    boolean unique() default false;

    boolean optional() default true;
}
