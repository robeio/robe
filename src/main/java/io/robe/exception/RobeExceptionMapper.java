package io.robe.exception;

import com.yammer.dropwizard.validation.InvalidEntityException;
import io.robe.admin.dto.BasicPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LOGGER.error("Exception", e);
        if (e instanceof RobeRuntimeException) {
            return ((RobeRuntimeException) e).getResponse();
        } else if (e instanceof InvalidEntityException) {
            InvalidEntityException exception = (InvalidEntityException) e;
            BasicPair[] errors = new BasicPair[exception.getErrors().size()];
            int i = 0;
            for (String error : exception.getErrors()) {
                String[] parts = error.split("\\.");
                // TODO Find a good way for showing InvalidEntityExceptions
                if (parts.length > 1)
                    errors[i++] = new BasicPair(parts[0], parts[1].split("\\(")[0]);
                else
                    errors[i++] = new BasicPair(exception.getMessage(), error);
            }
            return Response.status(422).entity(errors).type(MediaType.APPLICATION_JSON).build();
        } else if (e instanceof WebApplicationException) {
            return Response.status(((WebApplicationException) e).getResponse().getStatus()).type(MediaType.APPLICATION_JSON).build();
        } else {
            BasicPair[] errors = new BasicPair[1];
            errors[0] = new BasicPair("Server Error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
