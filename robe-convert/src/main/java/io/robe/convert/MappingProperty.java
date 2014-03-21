package io.robe.convert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kaanalkim on 20/03/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingProperty {
    int order();

    String name();
}
