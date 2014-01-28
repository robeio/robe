package io.robe.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.validation.InvalidEntityException;
import io.robe.auth.Credentials;
import io.robe.dto.User;
import io.robe.exception.RobeRuntimeException;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Role;
import io.robe.utils.HashingUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	public List<User> getUsers(@Auth Credentials credentials) {
		List<io.robe.hibernate.entity.User> entities = userDao.findAll(io.robe.hibernate.entity.User.class);
		List<User> users = new LinkedList<User>();
		for (io.robe.hibernate.entity.User entity : entities) {
			User user = null;
			try {
				user = new User(entity);
			} catch (Exception e) {
				throw new  RobeRuntimeException(e);
			}
			users.add(user);
		}
		return users;
	}

	@GET
	@UnitOfWork
	@Path("{userId}")
	public User get(@Auth Credentials credentials, @PathParam("userId") String id) {
		try {
			return new User(userDao.findById(id));
		} catch (Exception e) {
			throw new RobeRuntimeException(e);
		}
	}

	@PUT
	@UnitOfWork
	public User create(@Auth Credentials credentials, @Valid User user) {
		Optional<io.robe.hibernate.entity.User> checkUser = userDao.findByEmail(user.getEmail());
		if (checkUser.isPresent())
			throw new InvalidEntityException("E-mail", Arrays.asList(user.getEmail() + " already used by another user. Please use different e-mail."));
		User entity = new User();
		try {
			BeanUtils.copyProperties(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		Role role = roleDao.findById(user.getRoleOid());
		if (role == null) {
			throw new InvalidEntityException("Role", Arrays.asList(user.getEmail() + " cannot be null."));
		}
		// Copy all properties to entity. Set Role and firm. Update.
		try {
			BeanUtils.copyProperties(entity, user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		entity.setRole(role);
		entity.setPassword(HashingUtils.hashSHA2(user.getPassword()));
		try {
			return new User(userDao.create(entity));
		} catch (Exception e) {
			throw new RobeRuntimeException(e);
		}
	}

	@POST
	@UnitOfWork
	public User update(@Auth Credentials credentials, @Valid User user) {
		// Get and check user
		io.robe.hibernate.entity.User entity = userDao.findById(user.getOid());
		if (entity == null)
			throw new InvalidEntityException("User", Arrays.asList(user.getOid() + " not exists."));
		//Get role and firm and check for null
		Role role = roleDao.findById(user.getRoleOid());
		if (role == null) {
			throw new InvalidEntityException("Role", Arrays.asList(user.getEmail() + " cannot be null."));
		}
		// Copy all properties to entity. Set Role and firm. Update.
		try {
			BeanUtils.copyProperties(entity, user);
		} catch (Exception e) {
			throw new RobeRuntimeException(e);
		}
		entity.setRole(role);
		entity.setPassword(HashingUtils.hashSHA2(user.getPassword()));

		entity = userDao.update(entity);
		userDao.flush();

		try {
			return new User(entity);
		} catch (Exception e) {
			throw new RobeRuntimeException(e);
		}
	}


	@DELETE
	@UnitOfWork
	public User delete(@Auth Credentials credentials, User user) {

		userDao.delete(user);
		return user;
	}

}
