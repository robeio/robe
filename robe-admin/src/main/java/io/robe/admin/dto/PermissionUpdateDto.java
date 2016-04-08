package io.robe.admin.dto;

import java.util.List;

/**
 * Created by hasanmumin on 08/04/16.
 */
public class PermissionUpdateDto {

    private List<String> menus;
    private List<String> services;

    public List<String> getMenus() {
        return menus;
    }

    public void setMenus(List<String> menus) {
        this.menus = menus;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
}
