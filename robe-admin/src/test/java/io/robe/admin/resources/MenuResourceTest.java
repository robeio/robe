package io.robe.admin.resources;

import io.robe.admin.dto.MenuItem;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

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
    public void assertEquals(Menu mergeInstance, Menu original, Menu response) {
        Assert.assertEquals(mergeInstance.getIcon(), response.getIcon());
        Assert.assertEquals(original.getIndex(), response.getIndex());
        Assert.assertEquals(original.getPath(), response.getPath());
        Assert.assertEquals(original.getText(), response.getText());
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

    @Override
    public Menu mergeInstance() {

        Menu menu = new Menu();
        menu.setIcon("icon-1");
        return menu;
    }

    @Test
    public void getUserHierarchicalMenu() throws IOException {
        TestRequest request = getRequestBuilder().endpoint("user").build();
        TestResponse response = client.get(request);
        Assert.assertEquals(response.getStatus(), 200);
        List<MenuItem> menuItems = response.list(MenuItem.class);
        Assert.assertTrue(menuItems.size() > 0);
    }
}
