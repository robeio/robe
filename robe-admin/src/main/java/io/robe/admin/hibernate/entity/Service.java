package io.robe.admin.hibernate.entity;

import io.robe.auth.data.entry.ServiceEntry;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Service extends BaseEntity implements ServiceEntry {

    @Column(length = 100, nullable = false)
    private String path;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Method method;

    @Column(length = 32, name = "sGroup", nullable = false)
    private String group;

    @Column(length = 200, name = "sDescription")
    private String description;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
