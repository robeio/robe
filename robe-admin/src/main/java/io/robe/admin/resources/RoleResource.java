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


@Path("role")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {


    @Inject
    RoleDao roleDao;

    @Path("all")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET,flushMode = FlushMode.MANUAL)
    public List<Role> getRoles(@Auth Credentials credentials) {
        List<Role> roles = roleDao.findAll(Role.class);
        for (Role role : roles) {
            Hibernate.initialize(role.getRoles());
        }
        return roles;
    }

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET,flushMode = FlushMode.MANUAL)
    @Path("{userId}")
    public Role get(@Auth Credentials credentials, @PathParam("userId") String id) {
        Role role = roleDao.findById(id);
        initializeItems(role.getRoles());
        return role;
    }

    @PUT
    @UnitOfWork
    public Role create(@Auth Credentials credentials, @Valid Role role) {
        Optional<Role> checkRole = roleDao.findByName(role.getCode());
        if (checkRole.isPresent()) {
            throw new RobeRuntimeException("Code", role.getCode() + " already used by another role. Please use different code.");
        }
        return roleDao.create(role);
    }

    @POST
    @UnitOfWork
    public Role update(@Auth Credentials credentials, Role role) {

        roleDao.detach(role);
        Optional<Role> checkRole = roleDao.findByNameAndNotEqualMe(role.getCode(), role.getOid());
        if (checkRole.isPresent()) {
            throw new RobeRuntimeException("Code", role.getCode() + " already used by another role. Please use different code.");
        }
        Role entity = roleDao.findById(role.getOid());
        entity.setName(role.getName());
        entity.setCode(role.getCode());
        entity = roleDao.update(entity);
        return entity;

    }


    @DELETE
    @UnitOfWork
    public Role delete(@Auth Credentials credentials, Role role) {
        Role roleCheck = roleDao.findById(role.getOid());
        if (roleCheck != null) {
            initializeItems(roleCheck.getRoles());
            roleDao.delete(roleCheck);
        }
        return roleCheck;
    }

    @PUT
    @UnitOfWork
    @Path("group/{groupOid}/{roleOid}")
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

    @DELETE
    @UnitOfWork
    @Path("destroyRoleGroup/{groupOid}/{roleOid}")
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
