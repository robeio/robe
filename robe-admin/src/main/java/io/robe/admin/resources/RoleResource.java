package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hibernate.CacheMode.GET;


@Path("roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    private static final String ALREADY_USED = " already used by another role. Please use different code.";

    @Inject
    private RoleDao roleDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Role> getAll(@Auth Credentials credentials) {
        List<Role> roles = roleDao.findAll(Role.class);
        for (Role role : roles) {
            Hibernate.initialize(role.getRoles());
        }
        return roles;
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Role get(@Auth Credentials credentials, @PathParam("id") String id) {
        Role role = roleDao.findById(id);
        initializeItems(role.getRoles());
        return role;
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Role update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role role) {

        roleDao.detach(role);
        Optional<Role> checkRole = roleDao.findByNameAndNotEqualMe(role.getCode(), role.getOid());
        if (checkRole.isPresent()) {
            throw new RobeRuntimeException("Code", role.getCode() + ALREADY_USED);
        }
        Role entity = roleDao.findById(role.getOid());
        entity.setName(role.getName());
        entity.setCode(role.getCode());
        entity = roleDao.update(entity);
        return entity;

    }

    @POST
    @UnitOfWork
    public Role create(@Auth Credentials credentials, @Valid Role role) {
        Optional<Role> checkRole = roleDao.findByName(role.getCode());
        if (checkRole.isPresent()) {
            throw new RobeRuntimeException("Code", role.getCode() + ALREADY_USED);
        }
        return roleDao.create(role);
    }

    @Path("group/{groupOid}/{roleOid}")
    @PUT
    @UnitOfWork
    public Role createRoleGroup(@Auth Credentials credentials, @PathParam("groupOid") String groupOid, @PathParam("roleOid") String roleOid) {
        Role group = roleDao.findById(groupOid);
        checkNotNull(group, "groupOid mustn't be null");
        Role role = roleDao.findById(roleOid);
        checkNotNull(role, "roleOid mustn't be null");
        initializeItems(role.getRoles());
        initializeItems(group.getRoles());

        boolean included = isRoleIncludedAsSubRole(role, groupOid);

        if (included) {
            throw new RobeRuntimeException("Circular Dependency", "Operation failed.");
        }
        if (group.getRoles() == null) {
            group.setRoles(new HashSet<Role>());
        }

        group.getRoles().add(role);

        roleDao.update(group);

        return group;
    }

    private void initializeItems(Set<Role> roles) {
        for (Role role : roles) {
            Hibernate.initialize(role.getRoles());
            initializeItems(role.getRoles());
        }
    }

    private boolean isRoleIncludedAsSubRole(Role role, String groupOid) {
        if (role.getOid().equals(groupOid)) {
            return true;
        }
        for (Role child : role.getRoles()) {
            if (isRoleIncludedAsSubRole(child, groupOid)) {
                return true;
            }
        }
        return false;
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Role delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role role) {
        Role roleCheck = roleDao.findById(role.getOid());
        if (roleCheck != null) {
            initializeItems(roleCheck.getRoles());
            roleDao.delete(roleCheck);
        }
        return roleCheck;
    }

    @Path("destroyRoleGroup/{groupOid}/{roleOid}")
    @DELETE
    @UnitOfWork
    public Role destroyRoleGroup(@Auth Credentials credentials, @PathParam("groupOid") String groupOid, @PathParam("roleOid") String roleOid) {
        Role group = roleDao.findById(groupOid);
        Role role = roleDao.findById(roleOid);
        initializeItems(group.getRoles());
        if (group.getRoles() != null && group.getRoles().contains(role)) {
            group.getRoles().remove(role);
            roleDao.update(group);
        }
        return group;
    }
}
