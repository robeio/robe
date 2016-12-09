package io.robe.common.exceptions;

import io.robe.common.dto.BasicPair;
import io.robe.common.exception.RobeExceptionMapper;
import io.robe.common.exception.RobeRuntimeException;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by adem on 11/10/2016.
 */
public class RobeExceptionMapperTest {

    private final RobeExceptionMapper mapper = new RobeExceptionMapper();

    @Test
    public void robeRuntimeException() {
        RobeRuntimeException e = new RobeRuntimeException(new NullPointerException());
        Response response = mapper.toResponse(e);
        assertEquals(response, e.getResponse());
    }

    @Test
    public void constraintViolationException() {
       //TODO
    }

    @Test
    public void hibernateConstraintViolationException() {
        SQLException sqlEx = new SQLException("reason", "state");
        org.hibernate.exception.ConstraintViolationException e =
                new org.hibernate.exception.ConstraintViolationException("constraint violated", sqlEx, "index_email");
        BasicPair entity = new BasicPair("Unique Field Error", e.getCause().getMessage());
        Response response = mapper.toResponse(e);
        assertEquals(response.getStatus(), 409);
        assertEquals(response.getMediaType(), MediaType.valueOf("application/json"));
        assertEquals(response.getEntity(), entity);
    }


    @Test
    public void webApplicationException() {
        Response response = mapper.toResponse(new WebApplicationException(new NullPointerException("passowrd is null"), 401));
        assertEquals(response.getStatus(), 401);
        assertEquals(response.getMediaType(), MediaType.valueOf("application/json"));
    }

    @Test
    public void other() {
        Response response = mapper.toResponse(new IllegalStateException("channel is already open"));
        assertEquals(response.getStatus(), 500);
        assertEquals(response.getMediaType(), MediaType.valueOf("application/json"));
        assertEquals(((BasicPair[]) response.getEntity())[0], new BasicPair("Server Error", "channel is already open"));
    }

}
