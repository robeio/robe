package io.robe.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.validation.InvalidEntityException;
import io.robe.auth.Credentials;
import io.robe.dto.UserDTO;
import io.robe.exception.RobeRuntimeException;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Role;
import io.robe.hibernate.entity.User;
import io.robe.utils.HashingUtils;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


	@Inject
	UserDao userDao;
	@Inject
	RoleDao roleDao;

	@Path("all")
	@GET
	@UnitOfWork
	public List<UserDTO> getUsers(@Auth Credentials credentials) {
		List<io.robe.hibernate.entity.User> entities = userDao.findAll(io.robe.hibernate.entity.User.class);
		List<UserDTO> users = new LinkedList<UserDTO>();
		for (io.robe.hibernate.entity.User entity : entities) {
			UserDTO user = new UserDTO(entity);
			users.add(user);
		}
		return users;
	}

	@GET
	@UnitOfWork
	@Path("{userId}")
	public UserDTO get(@Auth Credentials credentials, @PathParam("userId") String id) {
		return new UserDTO(userDao.findById(id));
	}

	@PUT
	@UnitOfWork
	public UserDTO create(@Auth Credentials credentials, @Valid UserDTO user) {
		Optional<User> checkUser = userDao.findByEmail(user.getEmail());
		if (checkUser.isPresent())
			throw new RobeRuntimeException("E-mail", user.getEmail() + " already used by another user. Please use different e-mail.");
		User entity = new User();
		try {
			copyProperties(entity, user);
		} catch (IllegalAccessException e) {
			throw new RobeRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RobeRuntimeException(e);
		}
		Role role = roleDao.findById(user.getRoleOid());
		if (role == null) {
			throw new RobeRuntimeException("Role", user.getEmail() + " cannot be null.");
		}
		entity.setRole(role);
		entity.setPassword(HashingUtils.hashSHA2(user.getPassword()));

		return new UserDTO(userDao.create(entity));

	}

	@POST
	@UnitOfWork
	public UserDTO update(@Auth Credentials credentials, @Valid UserDTO user) {
		// Get and check user
		User entity = userDao.findById(user.getOid());
		if (entity == null)
			throw new InvalidEntityException("User", Arrays.asList(user.getOid() + " not exists."));
		//Get role and firm and check for null
		Role role = roleDao.findById(user.getRoleOid());
		if (role == null) {
			throw new InvalidEntityException("Role", Arrays.asList(user.getEmail() + " cannot be null."));
		}
		// Copy all properties to entity. Set Role and firm. Update.
		try {
			copyProperties(entity, user);
		} catch (Exception e) {
			throw new RobeRuntimeException(e);
		}
		entity.setRole(role);
		entity.setPassword(HashingUtils.hashSHA2(user.getPassword()));

		entity = userDao.update(entity);
		userDao.flush();

		return new UserDTO(entity);

	}

	@DELETE
	@UnitOfWork
	public UserDTO delete(@Auth Credentials credentials, UserDTO user) {
		User entity = userDao.findById(user.getOid());

		userDao.delete(entity);
		return user;
	}

}
