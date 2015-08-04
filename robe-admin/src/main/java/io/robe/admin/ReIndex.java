package io.robe.admin;

import io.robe.admin.hibernate.entity.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by serayuzgur on 28/07/15.
 */
public class ReIndex {

    public static void main(String[] args) {
        Menu userProfileManagement = new Menu();
        userProfileManagement.setCode("UserProfileManagement");
        userProfileManagement.setIndex(1);
        userProfileManagement.setName("Profil Yönetimi");

        Menu usermanagement = new Menu();
        usermanagement.setCode("UserManagement");
        usermanagement.setIndex(1);
        usermanagement.setName("Kullanıcı Yönetimi");

        Menu rolemanagement = new Menu();
        rolemanagement.setCode("RoleManagement");
        rolemanagement.setIndex(1);
        rolemanagement.setName("Rol Yönetimi");

        Menu menumanagement = new Menu();
        menumanagement.setCode("MenuManagement");
        menumanagement.setIndex(1);
        menumanagement.setName("Menü Yönetimi");

        Menu permissionManagement = new Menu();
        permissionManagement.setCode("PermissionManagement");
        permissionManagement.setIndex(1);
        permissionManagement.setName("İzin Atama");

        Menu dash = new Menu();
        dash.setCode("Dashboard");
        dash.setIndex(0);
        dash.setName("Dash");

        List<Menu>items = Arrays.asList(usermanagement,userProfileManagement,rolemanagement,menumanagement,permissionManagement,dash);
        Menu target = new Menu();
        target.setCode("Dashboard");
        target.setIndex(2);
        target.setName("Dash");

        ArrayList<Menu> ordered = reorderIndexes(target,items);

        for(Menu item : ordered)
            System.out.println(item.getIndex() + " : " + item.getCode());



    }

    private static  ArrayList<Menu> reorderIndexes(Menu target, List<Menu> items) {
        int targetIndex = target.getIndex();
        int index = 0;
        ArrayList<Menu> ordered = new ArrayList<>(items.size());
        items.remove(target);

        for(Menu item : items){
            if(target.getCode().equals(item.getCode()))
                continue;
            if(index == targetIndex){
                ordered.add(target);
                index++;
            }

            item.setIndex(index);
            ordered.add(item);
            index++;
        }
        return ordered;
    }

}
