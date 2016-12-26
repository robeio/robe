package io.robe.admin.resources;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.setup.Environment;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import org.hibernate.FlushMode;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@Path("metrics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetricsResource {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(
            new MetricsModule(
                    TimeUnit.SECONDS,
                    TimeUnit.SECONDS,
                    false,
                    MetricFilter.ALL));
    @Inject
    Environment environment;

    @GET
    @UnitOfWork(transactional = false, readOnly = true, flushMode = FlushMode.MANUAL)
    public Response get(@RobeAuth Credentials credentials) {
        Response.ResponseBuilder builder = Response.ok();
        builder.status(Response.Status.ACCEPTED);
        try {
            builder.entity(OBJECT_MAPPER.writeValueAsString(environment.metrics()));
        } catch (JsonProcessingException e) {
            builder.status(Response.Status.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        return builder.build();
    }


}


