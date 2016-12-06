package io.robe.hibernate.test.entity;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table
public class Permission extends BaseEntity {

    private String name;
    private String description;
    @Column(length = 32, nullable = false)
    private String roleOid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleOid() {
        return roleOid;
    }

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }
}
