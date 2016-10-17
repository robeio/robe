package io.robe.admin.resources;

import io.robe.admin.dto.PermissionUpdateDto;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import io.robe.auth.data.entry.PermissionEntry;
import io.robe.common.service.search.model.SearchModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by adem on 17/10/2016.
 */
public class PermissionResourceTest extends BaseResourceTest<Permission> {

    private final TestRequest.Builder roleRequestBuilder = new TestRequest.Builder(BASE_URL + "roles");
    private final TestRequest.Builder menuRequestBuilder = new TestRequest.Builder(BASE_URL + "menus");

    @Override
    public String getPath() {
        return "permissions";
    }

    @Override
    public Class<Permission> getClazz() {
        return Permission.class;
    }

    @Override
    public void assertEquals(Permission model, Permission response) {
        Assert.assertEquals(model.getRoleOid(), response.getRoleOid());
        Assert.assertEquals(model.getType(), response.getType());
        Assert.assertEquals(model.getRestrictedItemOid(), response.getRestrictedItemOid());
    }

    @Override
    public void assertEquals(Permission mergeInstance, Permission original, Permission response) {
        Assert.assertEquals(mergeInstance.getPriorityLevel(), response.getPriorityLevel());
        Assert.assertEquals(original.getRoleOid(), response.getRoleOid());
    }

    @Override
    public Permission instance() {
        Role role;
        try {
            role = getRoleByCode("Admin");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Permission permission = new Permission();
        permission.setType(PermissionEntry.Type.SERVICE);
        permission.setPriorityLevel((short) 1);
        permission.setRoleOid(role.getOid());
        permission.setRestrictedItemOid("oid");
        return permission;
    }

    @Override
    public Permission update(Permission permission) {
        permission.setType(PermissionEntry.Type.MENU);
        return permission;
    }

    @Override
    public Permission mergeInstance() {
        Permission permission = new Permission();
        permission.setPriorityLevel((short) 2);
        return permission;
    }

    @Test
    public void group() throws Exception {
        TestResponse response = client.get(getRequestBuilder().endpoint("group/" + "test").build());
        Map<String, Object> permissions = response.get(Map.class);
        Assert.assertNotNull(permissions);
        Assert.assertNotNull(permissions.get("menu"));
        Assert.assertNotNull(permissions.get("service"));
    }

    @Test
    public void updateServiceAndMenuPermissions() throws Exception {
        PermissionUpdateDto dto = new PermissionUpdateDto();
        Role role = getRoleByCode("Admin");

        List<String> menuOids = new ArrayList<>();
        menuOids.add(createMenu().getOid());
        dto.setMenus(menuOids);

        TestResponse response = client.get(roleRequestBuilder.endpoint(role.getOid() + "/permissions").build());
        Assert.assertEquals(200, response.getStatus());
        Map<String, List<Map<String, String>>> permissions = response.get(Map.class);
        Assert.assertNotNull(permissions);
        Assert.assertNotNull(permissions.get("menu"));
        Assert.assertNotNull(permissions.get("service"));

        List<String> serviceOids = new ArrayList<>();

        for(int i = 0; i < permissions.get("service").size(); i++) {
            Map<String, String> serviceEntry = permissions.get("service").get(i);
            serviceOids.add(serviceEntry.get("oid"));
        }

        dto.setServices(serviceOids);

        TestResponse res = client.post(getRequestBuilder().endpoint(role.getOid()).entity(dto).build());
        Assert.assertEquals(200, res.getStatus());

        res = client.post(getRequestBuilder().endpoint(role.getOid()).entity(dto).build());
        Assert.assertEquals(200, res.getStatus());

        TestRequest req = getRequestBuilder().endpoint("group/" + "Permission").build();
        response = client.get(req);
        Assert.assertEquals(200, response.getStatus());
        permissions = response.get(Map.class);
        Assert.assertNotNull(permissions);
        Assert.assertNotNull(permissions.get("menu"));
        Assert.assertNotNull(permissions.get("service"));

        List<Map<String, String>> menus = permissions.get("menu");
        List<Map<String, String>> services = permissions.get("service");
        Assert.assertEquals(2, menus.size());
        Assert.assertEquals(11, services.size());
        Map<String, String> menu = menus.get(0);
        Map<String, String> service = services.get(0);
        Assert.assertEquals(menu.get("module"), "Permission");
        Assert.assertEquals(service.get("group"), "Permission");
    }

    private Menu createMenu() throws Exception {
        Menu menu = new Menu();
        menu.setIndex(0);
        menu.setModule("Permission");
        menu.setPath("path");
        menu.setText("text");
        menu.setIcon("icon");
        return client.post(menuRequestBuilder.entity(menu).build()).get(Menu.class);
    }

    private Role getRoleByCode(String code) throws Exception {
        SearchModel searchModel = new SearchModel();
        searchModel.setQ(code);
        TestResponse response = client.get(roleRequestBuilder.search(searchModel).build());
        return response.list(Role.class).get(0);
    }

}
