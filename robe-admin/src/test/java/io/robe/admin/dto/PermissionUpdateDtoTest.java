package io.robe.admin.dto;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class PermissionUpdateDtoTest {

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
