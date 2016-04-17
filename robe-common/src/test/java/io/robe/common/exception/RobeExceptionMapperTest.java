package io.robe.common.exception;

import com.sun.jndi.cosnaming.ExceptionMapper;
import org.junit.Test;

import javax.ws.rs.core.Response;

/**
 * Created by Recep Köseoğlu on 17.4.2016.
 */
public class RobeExceptionMapperTest {


    @Test
    public void testToResponse() throws Exception {
        RobeExceptionMapper robeExceptionMapper=new RobeExceptionMapper();
        Exception exception=new RobeRuntimeException("robe runtime exception","robe runtime exceptiion message");
        Response response=robeExceptionMapper.toResponse(exception);

        assert Response.Status.INTERNAL_SERVER_ERROR.equals(response.getStatus()) ?true:false;
    }
}
