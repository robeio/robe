package io.robe.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.Credentials;
import io.robe.hibernate.dao.ServiceDao;
import io.robe.hibernate.entity.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {

	@Inject
	private ServiceDao serviceDao;

	@Path("/all")
	@GET
	@UnitOfWork
	public List<Service> getAll(@Auth Credentials credentials){

		return serviceDao.findAll(Service.class);
	}
}
