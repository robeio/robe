package io.robe.admin.dto;

import java.util.LinkedList;

public class MenuItem {
    private String text;
    private String cssClass;
    private List<MenuItem> items = new LinkedList<MenuItem>();
    private boolean expanded = true;

    public MenuItem(String text, String cssClass) {
        this.cssClass = cssClass;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getCssClass() {
        return "command:" + cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
