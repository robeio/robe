package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.caching.CacheControl;
import io.robe.admin.dto.MenuItem;
import io.robe.admin.hibernate.dao.*;
import io.robe.admin.hibernate.entity.*;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static org.hibernate.CacheMode.GET;

@Path("menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    private MenuDao menuDao;

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @Inject
    private UserDao userDao;

    /**
     * Returns all {@link Menu}s as a collection.
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @return all {@link Menu}s as a collection with.
     */

    @RobeService(group = "Menu", description = "Returns all Menu's as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Menu> getAll(@Auth Credentials credentials) {
        return menuDao.findAll();
    }


    private void getAllRolePermissions(Role parent, Set<Permission> rolePermissions) {
        rolePermissions.addAll(permissionDao.findByRoleOId(parent.getId()));
        List<RoleGroup> roleGroupEntries = roleGroupDao.findByGroupOId(parent.getId());
        for (RoleGroup entry : roleGroupEntries) {
            Role role = roleDao.findById(entry.getRoleOid());
            getAllRolePermissions(role, rolePermissions);
        }
    }

    /**
     * get menu for logged user
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @return user {@link MenuItem} as collection
     */

    @RobeService(group = "Menu", description = "Get menu for logged user")
    @Path("user")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    @CacheControl(noCache = true)
    public List<MenuItem> getUserHierarchicalMenu(@Auth Credentials credentials) {
        Optional<User> user = userDao.findByUsername(credentials.getUsername());
        Set<Permission> permissions = new HashSet<Permission>();

        Role parent = roleDao.findById(user.get().getRoleOid());
        getAllRolePermissions(parent, permissions);
        Set<String> menuOids = new HashSet<String>();

        List<MenuItem> items = convertMenuToMenuItem(menuDao.findHierarchicalMenu());
        items = readMenuHierarchical(items);

        for (Permission permission : permissions) {
            if (permission.getType().equals(Permission.Type.MENU)) {
                menuOids.add(permission.getRestrictedItemOid());
            }
        }
        List<MenuItem> permittedItems = new LinkedList<MenuItem>();

        createMenuWithPermissions(menuOids, items, permittedItems);

        return permittedItems;
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

    private void createMenuWithPermissions(Set<String> permissions, List<MenuItem> items, List<MenuItem> permittedItems) {
        for (MenuItem item : items) {
            MenuItem permittedItem = new MenuItem(item.getText(), item.getPath(), item.getModule(), item.getIndex(), item.getIcon());
            if (permissions.contains(item.getOid())) {
                permittedItems.add(permittedItem);
            }
            createMenuWithPermissions(permissions, item.getItems(), permittedItem.getItems());
            //If any sub menu permitted add parent menu also.
            if (!permittedItem.getItems().isEmpty() && !permittedItems.contains(permittedItem)) {
                permittedItems.add(permittedItem);
            }
        }
    }


    /**
     * Returns a single Menu matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @param id          This is the oid of {@link Menu}
     * @return a {@link Menu} resource matches with the given id.
     */
    @RobeService(group = "Menu", description = "Returns a Menu resource matches with the given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Menu get(@Auth Credentials credentials, @PathParam("id") String id) {
        Menu entity = menuDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create a {@link Menu} resource.
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @param model       data of {@link Menu}
     * @return Create as a {@link Menu} resource.
     */
    @RobeService(group = "Menu", description = "Create a Menu resource.")
    @POST
    @UnitOfWork
    public Menu create(@Auth Credentials credentials, @Valid Menu model) {
        return menuDao.create(model);
    }


    /**
     * Updates a single {@link Menu} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @param id          This is the oid of {@link Menu}
     * @param model       data of {@link Menu}
     * @return Updates a single {@link Menu} matches with the given id.
     */
    @RobeService(group = "Menu", description = "Updates a single Menu matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Menu update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Menu model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Menu entity = menuDao.findById(id);
        menuDao.detach(entity);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return menuDao.update(model);
    }


    /**
     * Updates a single {@link Menu} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @param id          This is the oid of {@link Menu}
     * @param model       data of {@link Menu}
     * @return Updates a single {@link Menu} matches with the given id.
     */
    @RobeService(group = "Menu", description = "Updates a single Menu matches with the given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Menu merge(@Auth Credentials credentials, @PathParam("id") String id, Menu model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Menu dest = menuDao.findById(id);
        menuDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return menuDao.update(model);
    }

    /**
     * Deletes a single {@link Menu} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link Auth} annotation for authentication.
     * @param id          This is the oid of {@link Menu}
     * @param model       data of {@link Menu}
     * @return delete a single {@link Menu} matches with the given id.
     */
    @RobeService(group = "Menu", description = "Deletes a single Menu matches with the given id.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Menu delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Menu model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Menu entity = menuDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return menuDao.delete(entity);
    }
}
