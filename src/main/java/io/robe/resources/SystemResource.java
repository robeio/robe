package io.robe.resources;

import com.yammer.dropwizard.auth.Auth;
import io.robe.auth.Credentials;
import io.robe.exception.RobeRuntimeException;
import io.robe.system.HeapDump;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

@Path("system")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {


    @GET
    @Path("heapdump")
    public Response getHeapDump(@Auth Credentials credentials) {

        File dumpFile = HeapDump.dump(false);

        final BufferedInputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(dumpFile));
            StreamingOutput stream = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    while (bufferedInputStream.available() > 0)
                        output.write(bufferedInputStream.read());
                }
            };
            return Response.ok().entity(stream).header("Content-Disposition", "attachment; filename=\"" + System.currentTimeMillis() + "\"").build();
        } catch (FileNotFoundException e) {
            throw new RobeRuntimeException(e);
        }

    }

}
