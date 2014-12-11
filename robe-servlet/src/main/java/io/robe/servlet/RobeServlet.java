package io.robe.servlet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to describe properties of {@link io.robe.servlet.ResourceServlet} at binding.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobeServlet {
    /**
     * Path of the {@link io.robe.servlet.ResourceServlet} to serve at.
     * @return
     */
    public String path();

    /**
     * Decides the usage style of {@link io.robe.servlet.ResourceServlet}.
     * If true only one instance will be created in wrapper.
     * Else every call will create new instance of {@link io.robe.servlet.ResourceServlet}
     * @return
     */
    public boolean singleton() default false;
}
