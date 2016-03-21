package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.robe.admin.dto.MenuItem;
import io.robe.admin.hibernate.dao.MenuDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

import static org.hibernate.CacheMode.GET;

@Path("menu")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    UserDao userDao;

    @Inject
    MenuDao menuDao;

    @Path("all")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Menu> getMenus(@Auth Credentials credentials) {

        List<Menu> menus = new ArrayList<>();
        for (Menu menu : menuDao.findAll(Menu.class)) {
            initializeItems(menu.getItems());
            menus.add(menu);
        }

        return menus;
    }

    private void getAllRolePermissions(Role parent, Set<Permission> rolePermissions) {
        rolePermissions.addAll(parent.getPermissions());
        for (Role role : parent.getRoles()) {
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
        getAllRolePermissions(user.get().getRole(), permissions);
        Set<String> menuOids = new HashSet<String>();
        List<Menu> items = menuDao.findHierarchicalMenu();
        for (Permission permission : permissions) {
            if (permission.getType().equals(Permission.Type.MENU)) {
                menuOids.add(permission.getRestrictedItemOid());
            }
        }
        List<MenuItem> permittedItems = new LinkedList<MenuItem>();

        createMenuWithPermissions(menuOids, items, permittedItems);

        return permittedItems;
    }

    private void createMenuWithPermissions(Set<String> permissions, List<Menu> items, List<MenuItem> permittedItems) {
        for (Menu item : items) {
            MenuItem permittedItem = new MenuItem(item.getName(), item.getCode(), item.getIndex());
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
    public List<Menu> getHierarchicalMenu(@Auth Credentials credentials) {
        List<Menu> menus = menuDao.findHierarchicalMenu();
        for (Menu menu : menus) {
            initializeItems(menu.getItems());
        }

        return menus;
    }

    private void initializeItems(List<Menu> menus) {
        for (Menu menu : menus) {
            Hibernate.initialize(menu.getItems());
            initializeItems(menu.getItems());
        }
    }

    @POST
    @UnitOfWork
    @Path("movenode/{item}/{destination}")
    public Menu move(@Auth Credentials credentials, @PathParam("item") String itemOid, @PathParam("destination") String destination) {
        String notValid = " is not valid.";
        Menu item = menuDao.findById(itemOid);
        Hibernate.initialize(item.getItems());
        Menu parent = menuDao.findById(destination);
        if (parent == null) {
            throw new RobeRuntimeException("destination", destination + notValid);
        }
        Hibernate.initialize(parent.getItems());
        reorderIndexes(item, parent.getItems());
        if (item == null) {
            throw new RobeRuntimeException("item", itemOid + notValid);
        } else if (itemOid.equals(destination)) {
            item.setParentOid(null);
        } else {
            item.setParentOid(destination);
        }
        item = menuDao.update(item);
        return item;

    }

    private ArrayList<Menu> reorderIndexes(Menu target, List<Menu> items) {
        int targetIndex = target.getIndex();
        int index = 0;
        ArrayList<Menu> ordered = new ArrayList<>(items.size());
        items.remove(target);

        for (Menu item : items) {
            if (target.getCode().equals(item.getCode()))
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

    @PUT
    @UnitOfWork
    public Menu create(@Auth Credentials credentials, @Valid Menu menu) {
        Optional<Menu> checkMenu = menuDao.findByCode(menu.getCode());
        if (checkMenu.isPresent()) {
            throw new RobeRuntimeException("Code", menu.getCode() + " already used by another menu. Please use different code.");
        }
        return menuDao.create(menu);
    }

    @POST
    @UnitOfWork
    public Menu update(@Auth Credentials credentials, Menu menu) {
        return menuDao.update(menu);

    }


    @DELETE
    @UnitOfWork
    public Menu delete(@Auth Credentials credentials, Menu menu) {
        Menu delete = menuDao.findById(menu.getOid());
        Hibernate.initialize(delete.getItems());
        menuDao.merge(delete);
        for (Menu child : delete.getItems()) {
            Hibernate.initialize(child.getItems());
            child.setParentOid(null);
            menuDao.update(child);
        }
        return menuDao.delete(delete);
    }
}
