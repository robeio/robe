package io.robe.common.exception;

import io.robe.common.dto.BasicPair;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * A customized {@link javax.ws.rs.WebApplicationException}.
 * Includes a {@link javax.ws.rs.core.Response} and {@link io.robe.common.dto.BasicPair}.
 * It helps to code clean and easy.
 */
public class RobeRuntimeException extends WebApplicationException {

    private final Response response;

    private final BasicPair entity;

    /**
     * Construct with the help of an exception.
     * Maps the exception as a cause to RobeRuntimeException and
     * creates a BasicPair with values as ("Exception", e.getMessage());
     *
     * @param e Exception to include.
     */
    public RobeRuntimeException(Exception e) {
        this("Exception", e.getMessage());
        this.initCause(e);
    }

    /**
     * Construct with the help of a name and an exception.
     * Maps the exception as a cause to RobeRuntimeException and
     * creates a BasicPair with values as (name, e.getMessage())
     *
     * @param name Name for the error.
     * @param e    Exception to include.
     */
    public RobeRuntimeException(String name, Exception e) {
        this(name, e.getMessage());
        this.initCause(e);
    }

    /**
     * Construct with the help of an name and message pair.
     * Creates a BasicPair with values as (name, message)
     *
     * @param name    Name for the error.
     * @param message Message to include.
     */
    public RobeRuntimeException(String name, String message) {
        entity = new BasicPair(name, message);
        response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(entity).build();
    }

    /**
     * Gets current name
     *
     * @return current name
     */
    public String getName() {
        return entity.getName();
    }

    /**
     * Gets current message
     *
     * @return current message
     */
    public String getMessage() {
        return entity.getValue();
    }

    /**
     * @return {@link io.robe.admin.dto.BasicPair#toString()}
     */
    @Override
    public String toString() {
        return entity.toString();
    }

    /**
     * Gets current response created with exception parameters.
     *
     * @return current response
     */
    public Response getResponse() {
        return response;
    }
}
