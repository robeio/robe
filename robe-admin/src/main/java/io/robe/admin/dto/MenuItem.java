package io.robe.admin.dto;

import java.util.LinkedList;
import java.util.List;

public class MenuItem {
    private String text;
    private String command;
    private List<MenuItem> items = new LinkedList<MenuItem>();
    private boolean expanded = true;
    private int index;


    public MenuItem(String text, String command, int index) {
        this.command = command;
        this.text = text;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = true;
    }


}
