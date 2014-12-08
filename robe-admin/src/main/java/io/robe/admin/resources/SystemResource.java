package io.robe.admin.resources;

import io.dropwizard.auth.Auth;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import io.robe.common.system.HeapDump;
import io.robe.common.utils.BufferedStreamingOutput;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Path("system")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {


    private static final int ONE_MB = 1024 * 1024;

    @GET
    @Path("heapdump")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHeapDump(@Auth Credentials credentials) {

        File dumpFile = HeapDump.dump(false);

        final BufferedInputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(dumpFile));
            StreamingOutput stream = new BufferedStreamingOutput(bufferedInputStream, ONE_MB);
            return Response.ok().entity(stream).header("Content-Disposition", "attachment; filename=\"" + System.currentTimeMillis() + ".hprof\"").build();
        } catch (FileNotFoundException e) {
            throw new RobeRuntimeException(e);
        }

    }


}
