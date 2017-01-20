package io.robe.common.service.search;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kamilbukum on 20/01/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    String name();
}