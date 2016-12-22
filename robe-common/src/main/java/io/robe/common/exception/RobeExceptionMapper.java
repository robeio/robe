package io.robe.common.exception;

import io.robe.common.dto.BasicPair;
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
        if (e instanceof RobeRuntimeException) {
            LOGGER.error("RobeRuntimeException", e);
            return ((RobeRuntimeException) e).getResponse();
        } else if (e instanceof ConstraintViolationException) {
            LOGGER.error("InvalidEntityException", e);
            ConstraintViolationException exception = (ConstraintViolationException) e;
            BasicPair[] errors = new BasicPair[exception.getConstraintViolations().size()];
            int i = 0;
            for (ConstraintViolation error : exception.getConstraintViolations()) {
                errors[i++] = new BasicPair(error.getConstraintDescriptor().getValidationAppliesTo().name(), error.getMessage());
            }
            return Response.status(422).entity(errors).type(MediaType.APPLICATION_JSON).build();
        } else if (e instanceof WebApplicationException) {
            WebApplicationException we = (WebApplicationException) e;
            if (we.getResponse().getStatus() != Response.Status.UNAUTHORIZED.getStatusCode() &&
                    we.getResponse().getStatus() != Response.Status.FORBIDDEN.getStatusCode()) {
                LOGGER.error("WebApplicationException", e);
            }
            return we.getResponse();
        } else {

            if (e.getClass().getName().equals("org.hibernate.exception.ConstraintViolationException")) {
                String id = System.nanoTime()+"";
                LOGGER.error(id + "", e);
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    BasicPair error = new BasicPair(id, e.getCause().getMessage().split("for")[0]);
                    return Response.status(409).entity(error).type(MediaType.APPLICATION_JSON).build();
                }
            }

            LOGGER.error("Exception", e);
            BasicPair[] errors = new BasicPair[1];
            errors[0] = new BasicPair("Server Error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
