package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Menu;
import org.junit.Assert;

/**
 * Created by hasanmumin on 04/10/16.
 */
public class MenuResourceTest extends BaseResourceTest<Menu> {

    @Override
    public String getPath() {
        return "menus";
    }

    @Override
    public Class<Menu> getClazz() {
        return Menu.class;
    }

    @Override
    public void assertEquals(Menu model, Menu response) {
        Assert.assertEquals(model.getIcon(), response.getIcon());
        Assert.assertEquals(model.getIndex(), response.getIndex());
        Assert.assertEquals(model.getPath(), response.getPath());
        Assert.assertEquals(model.getText(), response.getText());
    }

    @Override
    public Menu instance() {

        Menu menu = new Menu();
        menu.setIcon("icon");
        menu.setIndex(0);
        menu.setModule("/app/custom/Custom");
        menu.setPath("Custom");
        menu.setText("Custom Menu");
        return menu;
    }

    @Override
    public Menu update(Menu response) {
        response.setText("Custome Menu-1");
        return response;
    }
}
