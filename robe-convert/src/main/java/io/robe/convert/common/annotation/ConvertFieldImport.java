package io.robe.convert.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to define convert-import properties of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertFieldImport {

    /**
     * Decides to ignore the field at export operations.
     *
     * @return
     */
    boolean ignore() default false;

}
