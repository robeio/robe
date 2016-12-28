package io.robe.common.exceptions;

import io.robe.common.dto.RobeMessage;
import io.robe.common.exception.RobeRuntimeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class RobeRuntimeExceptionTest {

    @Test
    public void constructWithException() {
        RobeRuntimeException e = new RobeRuntimeException(new NullPointerException("wtf is null"));
        assertException(e, "wtf is null");
    }

    @Test
    public void constructWithNameAndMessage() {
        RobeRuntimeException e = new RobeRuntimeException("something went wrong");
        assertException(e, "something went wrong");
    }

    private void assertException(RobeRuntimeException e, String message) {
        RobeMessage robeMessage = new RobeMessage.Builder().message(message).status(e.getResponse().getStatus()).build();
        assertEquals(e.getResponse().getStatus(), 500);
        assertEquals(e.getResponse().getEntity(), robeMessage);
        assertEquals(e.getMessage(), robeMessage.getMessage());
        assertEquals(e.toString(), robeMessage.toString());
    }

}
