package io.robe.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to make web socket resource discoverable.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobeWebSocket {
    /**
     * Path of the web socket. (Must be unique)
     * @return
     */
    String path();

    /**
     * Include subPaths
     * @return
     */
    boolean subPaths() default true ;

}
