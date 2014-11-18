package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.auth.Credentials;
import io.robe.quartz.job.hibernate.JobEntity;
import org.hibernate.Hibernate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("quartzJob")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {
    @Inject
    QuartzJobDao quartzJobDao;

    @GET
    @UnitOfWork
    public List<JobEntity> getAll(@Auth Credentials credentials) {
        List<JobEntity> jobEntities = quartzJobDao.findAll(JobEntity.class);
        for (JobEntity jobEntity : jobEntities) {
            Hibernate.initialize(jobEntity.getTriggers());
        }
        return jobEntities;
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public JobEntity setCron(JobEntity quartzJob) {
        quartzJobDao.update(quartzJob);
        return quartzJob;
    }
}
