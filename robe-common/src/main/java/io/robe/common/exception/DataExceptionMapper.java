package io.robe.common.exception;

import io.dropwizard.jersey.errors.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by hasanmumin on 24/10/2016.
 */

@Provider
public class DataExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataExceptionMapper.class);

    @Override
    public Response toResponse(Throwable throwable) {
        LOGGER.error("Global error", throwable);
        String message = throwable.getMessage();

        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorMessage(Response.Status.CONFLICT.getStatusCode(), message))
                .build();
    }
}
