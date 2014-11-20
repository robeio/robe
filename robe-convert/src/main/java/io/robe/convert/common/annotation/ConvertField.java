package io.robe.convert.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to define convert properties of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertField {

    /**
     * Position of the field
     *
     * @return
     */
    int order() default 0;

    /**
     * Uniqueness of the field.
     *
     * @return true if the field must be unique.
     */
    boolean unique() default false;

    /**
     * Decides the field is optional or not.
     *
     * @return
     */
    boolean optional() default true;

    /**
     * Max length of the field. -1 means no limit
     *
     * @return
     */
    int maxLength() default -1;

    /**
     * Min length of the field. -1 means no limit.
     *
     * @return
     */
    int minLength() default -1;

    /**
     * Decides to ignore the field at export operations.
     *
     * @return
     */
    boolean ignore() default false;

}
