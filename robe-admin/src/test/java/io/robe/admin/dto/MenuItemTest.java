package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.Menu;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class MenuItemTest {

    @Test
    public void menuItem() {
        MenuItem item = new MenuItem();
        Assert.assertTrue(item.getItems().size() == 0);
        item.setItems(Collections.singletonList(new MenuItem()));
        Assert.assertTrue(item.getItems().size() == 1);
    }

    @Test
    public void menuItemWitMenu() {
        Menu menu = new Menu();
        menu.setText("text");
        menu.setPath("path");
        menu.setIcon("fa-users");
        menu.setIndex(0);
        menu.setModule("module");
        MenuItem item = new MenuItem(menu);

        Assert.assertEquals(item.getText(), menu.getText());
        Assert.assertEquals(item.getPath(), menu.getPath());
        Assert.assertEquals(item.getIcon(), menu.getIcon());
        Assert.assertEquals(item.getIndex(), menu.getIndex());
        Assert.assertEquals(item.getModule(), menu.getModule());
        Assert.assertTrue(item.getItems().size() == 0);
    }

    @Test
    public void menuItemWitConten() {
        MenuItem item = new MenuItem("text", "path", "module", 0, "fa-users");
        Assert.assertEquals(item.getText(), "text");
        Assert.assertEquals(item.getPath(), "path");
        Assert.assertEquals(item.getIcon(), "fa-users");
        Assert.assertEquals(item.getIndex(), 0);
        Assert.assertEquals(item.getModule(), "module");
        Assert.assertTrue(item.getItems().size() == 0);
    }
}
