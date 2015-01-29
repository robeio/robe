package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table
public class Menu extends BaseEntity {


    @Column(length = 50, nullable = false)
    private String code;

    @Column(length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "parentOid", fetch = FetchType.LAZY)
    @OrderBy("itemOrder ASC")
    private List<Menu> items = new LinkedList<Menu>();

    @Column
    private int itemOrder;


    @Column
    private String parentOid;

    @Transient
    private boolean expanded = true;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(int itemOrder) {
        this.itemOrder = itemOrder;
    }
}
