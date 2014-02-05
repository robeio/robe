package io.robe.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.validation.InvalidEntityException;
import io.robe.audit.Audited;
import io.robe.auth.Credentials;
import io.robe.dto.MenuItem;
import io.robe.hibernate.dao.MenuDao;
import io.robe.hibernate.dao.PermissionDao;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Menu;
import io.robe.hibernate.entity.Permission;
import io.robe.hibernate.entity.Role;
import io.robe.hibernate.entity.User;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("menu")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

	@Inject
	UserDao userDao;
	@Inject
	RoleDao roleDao;
	@Inject
	MenuDao menuDao;
	@Inject
	PermissionDao permissionDao;

	@Path("all")
	@GET
	@UnitOfWork
	public List<Menu> getMenus(@Auth Credentials credentials) {
		return menuDao.findAll(Menu.class);
	}

	private void getAllRolePermissions(Role parent, Set<Permission> rolePermissions) {
		rolePermissions.addAll(parent.getPermissions());
		for (Role role : parent.getRoles()) {
			getAllRolePermissions(role, rolePermissions);
		}
	}

	@Path("user")
	@GET
	@UnitOfWork
	public List<MenuItem> getUserHierarchicalMenu(@Auth Credentials credentials) {
		Optional<User> user = userDao.findByEmail(credentials.getUsername());
		Set<Permission> permissions = new HashSet<Permission>();
		getAllRolePermissions(user.get().getRole(), permissions);
		HashSet<String> menuOids = new HashSet<String>();
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

	private void createMenuWithPermissions(HashSet<String> permissions, List<Menu> items, List<MenuItem> permittedItems) {
		for (Menu item : items) {
			MenuItem permittedItem = new MenuItem(item.getName(), item.getCode());
			if (permissions.contains(item.getOid())) {
				permittedItems.add(permittedItem);
			}
			createMenuWithPermissions(permissions, item.getItems(), permittedItem.getItems());
			//If any sub menu permitted add parent menu also.
			if (permittedItem.getItems().size() > 0 && !permittedItems.contains(permittedItem))
				permittedItems.add(permittedItem);

		}
	}

	@Path("roots")
	@GET
	@UnitOfWork
	public List<Menu> getHierarchicalMenu(@Auth Credentials credentials) {
		List<Menu> menus = menuDao.findHierarchicalMenu();
		initializeItems(menus);
		return menus;
	}

	private void initializeItems(List<Menu> menus) {
		for (Menu menu : menus) {
			initializeItems(menu.getItems());
		}
	}

	@POST
	@UnitOfWork
	@Path("movenode/{item}/{destination}")
	public Menu move(@Auth Credentials credentials, @PathParam("item") String itemOid, @PathParam("destination") String parentOid) {
		Menu item = menuDao.findById(itemOid);
		Menu parent = menuDao.findById(parentOid);
		if (parent == null)
			throw new InvalidEntityException("destination", Arrays.asList(parentOid + " is not valid."));
		if (item == null)
			throw new InvalidEntityException("item", Arrays.asList(itemOid + " is not valid."));

		if (itemOid.equals(parentOid))
			item.setParentOid(null);
		else
			item.setParentOid(parentOid);
		item = menuDao.update(item);
		return item;

	}

	@PUT
	@UnitOfWork
	public Menu create(@Auth Credentials credentials, @Valid Menu menu) {
		Optional<Menu> checkMenu = menuDao.findByCode(menu.getCode());
		if (checkMenu.isPresent())
			throw new InvalidEntityException("Code", Arrays.asList(menu.getCode() + " already used by another menu. Please use different code."));
		return menuDao.create(menu);
	}

	@POST
	@UnitOfWork
	@Audited
	public Menu update(@Auth Credentials credentials, Menu menu) {
		return menuDao.update(menu);

	}


	@DELETE
	@UnitOfWork
	public Menu delete(@Auth Credentials credentials, Menu menu) {
		menuDao.delete(menu);
		return menu;
	}
}
