package io.robe.common.service.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchFrom {
    Class<?> entity();

    String [] select() default {};

    String[] filter() default {};

    String id() default "oid";

    String localId() default "";
}
