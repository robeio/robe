package io.robe.common.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * An annotation for describing robe services.
 */
public @interface RobeService {
    /**
     * Defines group for the service. All services with the same group will be matched with
     * a menu item if available.
     * @return name of the group.
     */
    String group() default "";

    /**
     * A brief description about the service.
     * @return description of the service.
     */
    String description() default "";

}
