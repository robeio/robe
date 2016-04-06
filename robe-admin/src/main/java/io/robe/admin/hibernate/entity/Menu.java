package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Menu extends BaseEntity {

    @Length(min = 2, max = 50)
    @NotEmpty
    @Column(length = 50, nullable = false)
    private String text;

    @Length(min =3, max = 100)
    @NotEmpty
    @Column(length = 100, nullable = false)
    private String path;


    @Column(name = "itemIndex")
    private int index;

    @Length(max = 32)
    @Column(length = 32)
    private String parentOid;

    @Length(max = 50)
    @Column(length = 50)
    private String module;

    @Length(max = 30)
    @Column(length = 30)
    private String icon;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
