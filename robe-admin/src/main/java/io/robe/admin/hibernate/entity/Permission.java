package io.robe.admin.hibernate.entity;

import io.robe.auth.data.entry.PermissionEntry;
import io.robe.common.service.search.SearchIgnore;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table
public class Permission extends BaseEntity implements PermissionEntry {

    @Enumerated(EnumType.STRING)
    @Column(name = "pType",nullable = false)
    private Type type;

    @SearchIgnore
    @Column(name = "restrictedItemOid", length = 32,nullable = false)
    private String restrictedItemOid;

    @Column(name = "pLevel",nullable = false)
    private short pLevel;

    @Column(length = 32,nullable = false)
    private String roleOid;

    public String getRoleOid() {
        return roleOid;
    }

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRestrictedItemOid() {
        return restrictedItemOid;
    }

    public void setRestrictedItemOid(String restrictedItemOid) {
        this.restrictedItemOid = restrictedItemOid;
    }

    public short getpLevel() {
        return pLevel;
    }

    public void setpLevel(short pLevel) {
        this.pLevel = pLevel;
    }

    @Override
    public String getRestrictedItemId() {
        return getRestrictedItemOid();
    }
}
