package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.Menu;

import java.util.LinkedList;
import java.util.List;

public class MenuItem extends Menu {

    private List<MenuItem> items = new LinkedList<MenuItem>();

    public MenuItem() {

    }

    public MenuItem(String text, String path, String module, int index) {
        setText(text);
        setPath(path);
        setModule(module);
        setIndex(index);
    }


    public MenuItem(Menu menu) {
        setOid(menu.getOid());
        setLastUpdated(menu.getLastUpdated());
        setText(menu.getText());
        setPath(menu.getPath());
        setIndex(menu.getIndex());
        setParentOid(menu.getParentOid());
        setIcon(menu.getIcon());
        setModule(menu.getModule());
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
