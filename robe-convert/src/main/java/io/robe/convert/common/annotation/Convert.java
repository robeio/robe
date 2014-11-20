package io.robe.convert.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to define convert properties of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert {

    public enum Operation {
        ALL,
        IMPORT,
        EXPORT
    }

    public enum Type {
        NONE,
        BOOL,
        BYTE,
        INT,
        LONG,
        DOUBLE,
        BIGDECIMAL,
        DATE
    }

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
     * Decides to ignore the field at  operations.
     *
     * @return
     */
    boolean ignore() default false;


    /**
     * Decides to default type of field for import and export .
     *
     * @return
     */
    Type type() default Type.NONE;

    /**
     * Column title for the field for exporting.
     *
     * @return
     */
    String title() default "";

    /**
     * Operation type. Default is {@link Convert.Operation.ALL}
     *
     * @return
     */
    Operation operation() default Operation.ALL;


}
