package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Menu;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class MenuDaoTest extends BaseDaoTest<Menu, MenuDao> {
    @Override
    public Menu instance() {

        Menu menu = new Menu();
        menu.setIcon("fa-users");
        menu.setIndex(0);
        menu.setParentOid(null);
        menu.setModule("MODULE");
        menu.setPath("/app/users/user");
        menu.setText("User Menu");
        return menu;
    }

    @Override
    public Menu update(Menu model) {
        model.setText("User Menu Updated");
        return model;
    }

    @Test
    public void findHierarchicalMenu() {
        List<Menu> menus = dao.findHierarchicalMenu();
        Assert.assertTrue(menus.size() == 1);
    }

    @Test
    public void findByModule() {
        super.createFrom();
        List<Menu> menus = dao.findByModule("MODULE");
        Assert.assertTrue(menus.size() == 1);
        super.deleteFrom(menus.get(0));
    }

    @Test
    public void findByParentOid() {
        Menu parent = super.createFrom();
        Menu child = instance();
        child.setParentOid(parent.getOid());
        child = super.createFrom(child);
        List<Menu> menus = dao.findByParentOid(parent.getOid());
        Assert.assertTrue(menus.size() == 1);

        Menu menu = menus.get(0);
        Assert.assertEquals(menu.getModule(), child.getModule());
        Assert.assertEquals(menu.getPath(), child.getPath());
        Assert.assertEquals(menu.getText(), child.getText());

        super.deleteFrom(child);
        super.deleteFrom(parent);

    }

}
