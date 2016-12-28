package io.robe.common.exceptions;

import io.robe.common.exception.RobeExceptionMapper;
import io.robe.common.exception.RobeRuntimeException;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


public class RobeExceptionMapperTest {

    private final RobeExceptionMapper mapper = new RobeExceptionMapper();

    @Test
    public void robeRuntimeException() {
        RobeRuntimeException e = new RobeRuntimeException("can not be empty");
        Response response = mapper.toResponse(e);
        assertEquals(response.toString(), e.getResponse().toString());
    }

    @Test
    public void hibernateConstraintViolationException() {
        SQLException sqlEx = new SQLException("reason", "state");
        org.hibernate.exception.ConstraintViolationException e =
                new org.hibernate.exception.ConstraintViolationException("constraint violated", sqlEx, "index_email");
        Response response = mapper.toResponse(e);
        assertEquals(response.getStatus(), 409);
        assertEquals(response.getMediaType(), MediaType.valueOf("application/json"));
    }


    @Test
    public void webApplicationException() {
        Response response = mapper.toResponse(new WebApplicationException(new NullPointerException("passowrd is null"), 401));
        assertEquals(response.getStatus(), 401);
    }

}
