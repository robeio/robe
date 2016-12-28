package io.robe.common.exception;

import io.robe.common.dto.RobeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Exception mapper for all exceptions thrown from application.
 * Aim of this mapper is to show  standardized JSON format for errors to the user..
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class RobeExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobeExceptionMapper.class);

    /**
     * Parses or wraps the exception and transforms it to a human readable json loaded response.
     *
     * @param e Exception to transform
     * @return error response
     */
    @Override
    public Response toResponse(Exception e) {

        String id = System.nanoTime() + "";
        LOGGER.error(id, e);

        if (e instanceof RobeRuntimeException) {
            return ((RobeRuntimeException) e).getResponse(id);
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) e;
            RobeMessage[] errors = new RobeMessage[exception.getConstraintViolations().size()];
            int i = 0;
            for (ConstraintViolation error : exception.getConstraintViolations()) {
                errors[i++] = new RobeMessage.Builder().message(error.getMessage()).status(422).id(id).build();
            }
            return Response.status(422).entity(errors).type(MediaType.APPLICATION_JSON).build();
        } else if (e instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException) e;
            RobeMessage error = new RobeMessage.Builder().id(id).message(we.getMessage()).status(we.getResponse().getStatus()).build();
            return Response.status(we.getResponse().getStatus()).entity(error).type(MediaType.APPLICATION_JSON).build();
        } else {
            if (e.getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    RobeMessage error = new RobeMessage.Builder().message(e.getCause().getMessage().split("for")[0]).status(Response.Status.CONFLICT.getStatusCode()).id(id).build();
                    return Response.status(Response.Status.CONFLICT).entity(error).type(MediaType.APPLICATION_JSON).build();
                }
            }
            RobeMessage error = new RobeMessage.Builder().message(e.getMessage()).id(id).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
