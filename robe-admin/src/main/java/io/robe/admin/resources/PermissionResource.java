package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MenuDao;
import io.robe.admin.hibernate.dao.PermissionDao;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.auth.token.BasicToken;
import org.hibernate.FlushMode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hibernate.CacheMode.GET;

@Path("permissions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource {

    @Inject
    private RoleDao roleDao;

    @Inject
    private MenuDao menuDao;

    @Inject
    private PermissionDao permissionDao;

    @Inject
    private ServiceDao serviceDao;


    @Path("{roleOid}/menu")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<String> getRoleHierarchicalMenu(@Auth Credentials credentials, @PathParam("roleOid") String roleOid) {
        Role role = roleDao.findById(roleOid);
        Set<Permission> permissions = role.getPermissions();
        List<String> menuOids = new LinkedList<String>();
        for (Permission permission : permissions) {
            if (permission.getType().equals(Permission.Type.MENU)) {
                menuOids.add(permission.getRestrictedItemOid());
            }
        }
        return menuOids;
    }

    @Path("{roleOid}/menu")
    @PUT
    @UnitOfWork
    public List<String> setRoleHierarchicalMenu(@Auth Credentials credentials, @PathParam("roleOid") String roleOid, List<String> items) {
        Role role = roleDao.findById(roleOid);
        List<String> menuOids = new LinkedList<String>();
        permissionDao.deleteRestrictionsByRole(role, Permission.Type.MENU);
        for (String itemOid : items) {
            Menu menu = menuDao.findById(itemOid);
            Permission permission = permissionDao.findByRoleAndItem(role, menu);
            if (permission == null) {
                permission = new Permission();
            }
            permission.setRole(role);
            permission.setType(Permission.Type.MENU);
            permission.setpLevel((short) 7);
            permission.setRestrictedItemOid(itemOid);
            permissionDao.create(permission);

        }
        return menuOids;
    }

    @Path("{roleOid}/service")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<String> getRoleServices(@Auth Credentials credentials, @PathParam("roleOid") String roleOid) {
        Role role = roleDao.findById(roleOid);
        Set<Permission> permissions = role.getPermissions();
        List<String> serviceOids = new LinkedList<String>();
        for (Permission permission : permissions) {
            if (permission.getType().equals(Permission.Type.SERVICE)) {
                serviceOids.add(permission.getRestrictedItemOid());
            }
        }
        return serviceOids;
    }

    @Path("{roleOid}/service")
    @PUT
    @UnitOfWork
    public List<String> setRoleService(@Auth Credentials credentials, @PathParam("roleOid") String roleOid, List<String> items) {
        Role role = roleDao.findById(roleOid);
        List<String> serviceOids = new LinkedList<String>();
        permissionDao.deleteRestrictionsByRole(role, Permission.Type.SERVICE);
        for (String itemOid : items) {
            Service service = serviceDao.findById(itemOid);
            Permission permission = permissionDao.findByRoleAndItem(role, service);
            if (permission == null) {
                permission = new Permission();
            }
            permission.setRole(role);
            permission.setType(Permission.Type.SERVICE);
            permission.setpLevel((short) 7);
            permission.setRestrictedItemOid(itemOid);
            permissionDao.create(permission);

        }

        BasicToken.clearPermissionCache(credentials.getUsername());

        return serviceOids;
    }
}
