package io.robe.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.Credentials;
import io.robe.exception.RobeRuntimeException;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.entity.Role;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("role")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {


	@Inject
	RoleDao roleDao;

	@Path("all")
	@GET
	@UnitOfWork
	public List<Role> getRoles(@Auth Credentials credentials) {
		return roleDao.findAll(Role.class);
	}

	@GET
	@UnitOfWork
	@Path("{userId}")
	public Role get(@Auth Credentials credentials, @PathParam("userId") String id) {
		return roleDao.findById(id);
	}

	@PUT
	@UnitOfWork
	public Role create(@Auth Credentials credentials, @Valid Role role) {
		Optional<Role> checkUser = roleDao.findByName(role.getCode());
		if (checkUser.isPresent())
			throw new RobeRuntimeException("Code", role.getCode() + " already used by another role. Please use different code.");
		role = roleDao.create(role);
		return role;
	}

	@POST
	@UnitOfWork
	public Role update(@Auth Credentials credentials, Role role) {
		role = roleDao.update(role);
		return role;

	}


	@DELETE
	@UnitOfWork
	public Role delete(@Auth Credentials credentials, Role role) {
		role = roleDao.findById(role.getOid());
		roleDao.delete(role);
		return role;
	}
}
