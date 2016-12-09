package io.robe.admin.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class PermissionUpdateDtoTest {
    ArrayList<String> menus;
    ArrayList<String> services;
    PermissionUpdateDto dto;

    @Before
    public void setUp() throws Exception {
        menus = new ArrayList<>();
        services = new ArrayList<>();

        menus.add("menu");
        services.add("sevice");

        dto = new PermissionUpdateDto();

    }

    @Test
    public void getMenus() throws Exception {
        dto.setMenus(menus);
        assertEquals(menus, dto.getMenus());

    }

    @Test
    public void getServies() throws Exception {
        dto.setServices(services);
        assertEquals(services, dto.getServices());

    }


    @Test
    public void permissionUpdateDto() {
        PermissionUpdateDto dto = new PermissionUpdateDto();

        List<String> menus = Arrays.asList("User", "Role");
        dto.setMenus(menus);
        List<String> services = Arrays.asList("UserService", "RoleService", "MenuService");
        dto.setServices(services);

        Assert.assertTrue(dto.getMenus().size() == 2);
        Assert.assertTrue(dto.getServices().size() == 3);
    }
}
