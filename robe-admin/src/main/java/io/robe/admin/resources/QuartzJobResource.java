package io.robe.admin.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.auth.Credentials;
import io.robe.quartz.QuartzJob;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("quartzJob")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {
    @Inject
    QuartzJobDao quartzJobDao;
    private static Logger LOGGER = Logger.getLogger(QuartzJobResource.class);

    @GET
    @UnitOfWork
    public List<QuartzJob> getAll(@Auth Credentials credentials) {
        List<QuartzJob> list = quartzJobDao.findAll(QuartzJob.class);
        return list;
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public QuartzJob setCron(QuartzJob quartzJob) {
        quartzJobDao.update(quartzJob);
        return quartzJob;
    }
}
