package io.robe.common.exceptions;

import io.robe.common.dto.BasicPair;
import io.robe.common.exception.RobeRuntimeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by adem on 11/10/2016.
 */
public class RobeRuntimeExceptionTest {

    @Test
    public void constructWithException() {
        RobeRuntimeException e = new RobeRuntimeException(new NullPointerException("wtf is null"));
        assertException(e, "RobeRuntimeException", "wtf is null");
    }

    @Test
    public void constructWithNameAndException() {
        RobeRuntimeException e = new RobeRuntimeException("warn", new NullPointerException("wtf is null"));
        assertException(e, "warn", "wtf is null");
    }

    @Test
    public void constructWithNameAndMessage() {
        RobeRuntimeException e = new RobeRuntimeException("warn", "something went wrong");
        assertException(e, "warn", "something went wrong");
    }

    @Test
    public void constructWithNameAndMessageAndException() {
        RobeRuntimeException e = new RobeRuntimeException("warn", "something went wrong", new NullPointerException("wtf is null"));
        assertException(e, "warn", "something went wrong");
    }

    private void assertException(RobeRuntimeException e, String name, String message) {
        BasicPair entity = new BasicPair(name, message);
        assertEquals(e.getResponse().getStatus(), 500);
        assertEquals(e.getResponse().getEntity(), entity);
        assertEquals(e.getName(), entity.getName());
        assertEquals(e.getMessage(), entity.getValue());
        assertEquals(e.toString(), entity.toString());
    }

}
