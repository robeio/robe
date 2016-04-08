package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.*;
import io.robe.admin.hibernate.entity.*;
import io.robe.auth.Credentials;
import io.robe.auth.data.entry.PermissionEntry;
import io.robe.common.service.RobeService;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.CacheMode.GET;

@Path("roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Inject
    private ServiceDao serviceDao;

    @Inject
    private MenuDao menuDao;


    /**
     * Returns all services and menus collection with the matches given Role id.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is Role oid
     * @return JSONObject (MENU and SERVICE)
     */

    @RobeService(group = "Permission", description = "Returns all services and menus collection with the matches given Role id.")
    @GET
    @Path("{id}/services/groups")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Map<String, Object> getServicesGroupByRole(@Auth Credentials credentials, @PathParam("id") String id) {

        List<Permission> permissions = new ArrayList<>();

        List<Service> services = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();

        Role role = roleDao.findById(id);

        getAllRolePermissions(role, permissions);

        for (Permission permission : permissions) {
            if (permission.getType().equals(PermissionEntry.Type.SERVICE)) {
                Service service = serviceDao.findById(permission.getRestrictedItemOid());
                if (service != null) {
                    if (services.indexOf(service) == -1) {
                        services.add(service);
                    }
                }

            } else if (permission.getType().equals(PermissionEntry.Type.MENU)) {

                Menu menu = menuDao.findById(permission.getRestrictedItemOid());
                if (menu != null) {
                    if (menus.indexOf(menu) == -1) {
                        menus.add(menu);
                    }
                }
            }
        }


        Map<String, Object> response = new HashMap<>();
        response.put("MENU", menus);
        response.put("SERVICE", services);

        return response;
    }

    private void getAllRolePermissions(Role parent, List<Permission> rolePermissions) {
        rolePermissions.addAll(permissionDao.findByRoleOId(parent.getId()));
        List<RoleGroup> roleGroupEntries = roleGroupDao.findByGroupOId(parent.getId());
        for (RoleGroup entry : roleGroupEntries) {
            Role role = roleDao.findById(entry.getRoleId());
            getAllRolePermissions(role, rolePermissions);
        }
    }

    /**
     * Return all Role as a collection
     *
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @return all {@link Role} as a collection
     */
    @RobeService(group = "Role", description = "Returns all Role as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Role> getAll(@Auth Credentials credentials) {
        return roleDao.findAll();
    }

    /**
     * Return a Role resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @return a  {@link Role} resource with the matches given id.
     */
    @RobeService(group = "Role", description = "Returns a Role resource with the matches given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Role get(@Auth Credentials credentials, @PathParam("id") String id) {
        Role entity = roleDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create a {@link Role} resource.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link Role}
     * @return create a {@link Role} resource.
     */
    @RobeService(group = "Role", description = "Create a Role resource.")
    @POST
    @UnitOfWork
    public Role create(@Auth Credentials credentials, @Valid Role model) {
        return roleDao.create(model);
    }

    /**
     * Update a Role resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Update a  {@link Role} resource with the matches given id.
     */
    @RobeService(group = "Role", description = "Update a Role resource with the matches given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Role update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Role entity = roleDao.findById(id);
        roleDao.detach(entity);

        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return roleDao.update(model);
    }

    /**
     * Update a Role resource with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Updates a  {@link Role} resource with the matches given id.
     */
    @RobeService(group = "Role", description = "Update a Role resource with the matches given id.")
    @Path("{id}")
    @PATCH
    @UnitOfWork
    public Role merge(@Auth Credentials credentials, @PathParam("id") String id, Role model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Role dest = roleDao.findById(id);
        roleDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return roleDao.update(model);
    }

    /**
     * Delete a Role resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Delete a  {@link Role} resource  with the matches given id.
     */
    @RobeService(group = "Role", description = "Delete a Role resource with the matches given id.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Role delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Role entity = roleDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return roleDao.delete(entity);
    }

}
