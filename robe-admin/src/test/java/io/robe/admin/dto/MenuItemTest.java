package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.Menu;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.ArrayList;

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
}
