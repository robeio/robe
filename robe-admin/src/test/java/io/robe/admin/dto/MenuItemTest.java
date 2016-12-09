package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.Menu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 01/10/16.
 */
@FixMethodOrder
public class MenuItemTest {
    ArrayList<MenuItem> items;
    MenuItem item;
    MenuItem item2;
    MenuItem item3;

    @Before
    public void setUp() throws Exception {
        items = new ArrayList<>();

        item = new MenuItem("text", "path", "module", 1, "icon");
        Menu menu = new Menu();
        menu.setText("text");
        menu.setPath("path");
        menu.setModule("module");
        menu.setIcon("icon");
        menu.setIndex(1);
        item2 = new MenuItem(menu);
        item3 = new MenuItem();

        items.add(item);
        items.add(item2);

    }

    @Test
    public void getItems() throws Exception {
        item3.setItems(items);
        assertEquals(items, item3.getItems());

    }


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
