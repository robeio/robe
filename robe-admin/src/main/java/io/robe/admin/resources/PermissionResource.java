package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.dto.MenuItem;
import io.robe.admin.dto.PermissionUpdateDto;
import io.robe.admin.hibernate.dao.MenuDao;
import io.robe.admin.hibernate.dao.PermissionDao;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.auth.token.BasicToken;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.CacheMode.GET;

@Path("permissions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource {

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private ServiceDao serviceDao;

    @Inject
    private MenuDao menuDao;


    /**
     * get hierarchical menu service for permission
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @return collection of hierarchical {@link MenuItem}
     */
    @RobeService(group = "Permission", description = "Get hierarchical menu service for permission")
    @Path("menus")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<MenuItem> getHierarchicalMenu(@RobeAuth Credentials credentials) {
        return readMenuHierarchical(convertMenuToMenuItem(menuDao.findHierarchicalMenu()));
    }


    private List<MenuItem> readMenuHierarchical(List<MenuItem> items) {
        for (MenuItem item : items) {
            List<Menu> menus = menuDao.findByParentOid(item.getOid());
            item.setItems(convertMenuToMenuItem(menus));
            readMenuHierarchical(item.getItems());
        }

        return items;
    }

    private List<MenuItem> convertMenuToMenuItem(List<Menu> menus) {
        List<MenuItem> items = new ArrayList<>();
        for (Menu menu : menus) {
            items.add(new MenuItem(menu));
        }
        return items;
    }

    /**
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param code        This names of Permission Group Code
     * @return JSONObject (MENU and SERVICE)
     */
    @RobeService(group = "Permission", description = "On select group list service and menu service")
    @Path("group/{groupCode}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Map<String, Object> getServicesByServiceGroup(@RobeAuth Credentials credentials, @PathParam("groupCode") String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("menu", menuDao.findByModule(code));
        response.put("service", serviceDao.findServiceByGroup(code));
        return response;
    }

    /**
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param updateDto   include selected menu and service
     * @param roleOid     selected oid of {@link io.robe.admin.hibernate.entity.Role}
     * @return OK.
     */
    @RobeService(group = "Permission", description = "Create Or Update Service And Menu")
    @POST
    @UnitOfWork
    @Path("{roleOid}")
    public Response createOrUpdateServiceAndMenu(@RobeAuth Credentials credentials, @Valid PermissionUpdateDto updateDto, @PathParam("roleOid") String roleOid) {

        // delete old menu
        permissionDao.deleteRestrictionsByRole(roleOid, Permission.Type.MENU);

        // save menu

        for (String itemOid : updateDto.getMenus()) {
            Menu menu = menuDao.findById(itemOid);
            Permission permission = permissionDao.findByRoleAndItem(roleOid, menu);
            if (permission == null) {
                permission = new Permission();
            }
            permission.setRoleOid(roleOid);
            permission.setType(Permission.Type.MENU);
            permission.setPriorityLevel((short) 7);
            permission.setRestrictedItemOid(itemOid);
            permissionDao.create(permission);
        }

        // delete old services
        permissionDao.deleteRestrictionsByRole(roleOid, Permission.Type.SERVICE);

        // save services
        for (String itemOid : updateDto.getServices()) {
            Service service = serviceDao.findById(itemOid);
            Permission permission = permissionDao.findByRoleAndItem(roleOid, service);
            if (permission == null) {
                permission = new Permission();
            }
            permission.setRoleOid(roleOid);
            permission.setType(Permission.Type.SERVICE);
            permission.setPriorityLevel((short) 7);
            permission.setRestrictedItemOid(itemOid);
            permissionDao.create(permission);

        }

        BasicToken.clearAllPermissionCache();

        return Response.ok().build();
    }


    /**
     * Return all {@link Permission}s as a collection.
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @return all @{@link Permission}s as a collection.
     */
    @RobeService(group = "Permission", description = "Return all permissions as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Permission> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        return permissionDao.findAll(search);
    }

    /**
     * Returns a {@link Permission} resource with the given id
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @return a @{@link Permission} resource macthes with the given id.
     */
    @RobeService(group = "Permission", description = "Returns a permission resource with the given id")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Permission get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Creates a {@link Permission} resource.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @param model       Data of {@link Permission}
     * @return Creates a @{@link Permission} resource.
     */
    @RobeService(group = "Permission", description = "Creates a permission resource.")
    @POST
    @UnitOfWork
    public Permission create(@RobeAuth Credentials credentials, @Valid Permission model) {
        return permissionDao.create(model);
    }

    /**
     * Updates a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return Updates a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Updates a permission resource matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Permission update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid Permission model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        permissionDao.detach(entity);
        return permissionDao.update(model);
    }

    /**
     * Updates a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return Updates a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Updates a permission resource matches with the given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Permission merge(@RobeAuth Credentials credentials, @PathParam("id") String id, Permission model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Permission dest = permissionDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return permissionDao.update(dest);
    }

    /**
     * Deletes a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return deletes a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Deletes a permission resource matches with the given id.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Permission delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid Permission model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return permissionDao.delete(entity);
    }
}
