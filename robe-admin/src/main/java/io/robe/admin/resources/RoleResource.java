package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.dao.RoleGroupDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.hibernate.entity.RoleGroup;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hibernate.CacheMode.GET;


@Path("roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    private static final String ALREADY_USED = " already used by another role. Please use different code.";

    @Inject
    private RoleDao roleDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Role> getAll(@Auth Credentials credentials) {
        return roleDao.findAll(Role.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Role get(@Auth Credentials credentials, @PathParam("id") String id) {

        Role response = roleDao.findById(id);

        return readRoleHierarchical(response);
    }

    private Role readRoleHierarchical(Role response) {

        List<RoleGroup> roleGroups = roleGroupDao.findByGroupOId(response.getOid());

        for (RoleGroup roleGroup : roleGroups) {
            Role role = roleDao.findById(roleGroup.getRoleOid());
            response.getRoles().add(readRoleHierarchical(role));
        }

        return response;
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

        throw new RobeRuntimeException("Error", "createRoleGroup not implemented yet");
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Role delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role role) {
        throw new RobeRuntimeException("Error", "delete not implemented yet");
    }

    @Path("destroyRoleGroup/{groupOid}/{roleOid}")
    @DELETE
    @UnitOfWork
    public Role destroyRoleGroup(@Auth Credentials credentials, @PathParam("groupOid") String groupOid, @PathParam("roleOid") String roleOid) {
        Role group = roleDao.findById(groupOid);
        Role role = roleDao.findById(roleOid);

        throw new RobeRuntimeException("Error", "destroyRoleGroup not implemented yet");

    }
}
