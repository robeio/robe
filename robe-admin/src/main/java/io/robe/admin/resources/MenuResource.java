package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.robe.admin.dto.MenuItem;
import io.robe.admin.hibernate.dao.*;
import io.robe.admin.hibernate.entity.*;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

import static org.hibernate.CacheMode.GET;

@Path("menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    private UserDao userDao;

    @Inject
    private MenuDao menuDao;

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private RoleGroupDao roleGroupDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Menu> getAll(@Auth Credentials credentials) {
        return menuDao.findAll(Menu.class);
    }

    private void getAllRolePermissions(Role parent, Set<Permission> rolePermissions) {
        rolePermissions.addAll(permissionDao.findByRoleOId(parent.getId()));
        List<RoleGroup> roleGroupEntries = roleGroupDao.findByGroupOId(parent.getId());
        for (RoleGroup entry : roleGroupEntries) {
            Role role = roleDao.findById(entry.getRoleOid());
            getAllRolePermissions(role, rolePermissions);
        }
    }

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
            MenuItem permittedItem = new MenuItem(item.getText(), item.getPath(), item.getModule(), item.getIndex());
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

    @Path("roots")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<MenuItem> getHierarchicalMenu(@Auth Credentials credentials) {
        List<MenuItem> items = readMenuHierarchical(convertMenuToMenuItem(menuDao.findHierarchicalMenu()));

        return items;
    }

    @Path("movenode/{item}/{destination}")
    @POST
    @UnitOfWork
    public Menu move(@Auth Credentials credentials, @PathParam("item") String itemOid, @PathParam("destination") String destination) {
        throw new RobeRuntimeException("ERROR", "Move not implemented yet!");
    }

    private ArrayList<Menu> reorderIndexes(Menu target, List<Menu> items) {
        int targetIndex = target.getIndex();
        int index = 0;
        ArrayList<Menu> ordered = new ArrayList<>(items.size());
        items.remove(target);

        for (Menu item : items) {
            if (target.getPath().equals(item.getPath()))
                continue;
            if (index == targetIndex) {
                ordered.add(target);
                index++;
            }

            item.setIndex(index);
            ordered.add(item);
            index++;
        }
        return ordered;
    }

    @POST
    @UnitOfWork
    public Menu create(@Auth Credentials credentials, @Valid Menu menu) {
        Optional<Menu> checkMenu = menuDao.findByCode(menu.getPath());
        if (checkMenu.isPresent()) {
            throw new RobeRuntimeException("Path", menu.getPath() + " already used by another menu. Please use different path.");
        }
        return menuDao.create(menu);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Menu update(@Auth Credentials credentials, @PathParam("id") String id, Menu menu) {
        return menuDao.update(menu);

    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Menu delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Menu menu) {
        Menu delete = menuDao.findById(menu.getOid());
        return menuDao.delete(delete);
    }
}
