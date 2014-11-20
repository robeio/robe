package io.robe.convert.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to define convert-export properties of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertFieldExport {

    /**
     * Decides to ignore the field at export operations.
     *
     * @return
     */
    boolean ignore() default false;

    /**
     * Column name for the field for exporting.
     *
     * @return
     */
    String columnTitle() default "";

    /**
     * Column width for the export. -1 means width will be decided according to column name width (Only works for XLS,XLSX)
     *
     * @return
     */
    int columnWidth() default -1;
}
