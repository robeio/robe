package io.robe.admin.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.auth.data.entry.RoleGroupEntry;
import io.robe.common.service.search.SearchIgnore;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class RoleGroup extends BaseEntity implements RoleGroupEntry {

    @SearchIgnore
    @Column(length = 32)
    private String roleOid;
    @SearchIgnore
    @Column(length = 32)
    private String groupOid;

    public String getRoleOid() {
        return roleOid;
    }

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }

    public String getGroupOid() {
        return groupOid;
    }

    public void setGroupOid(String groupOid) {
        this.groupOid = groupOid;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return getOid();
    }

    @JsonIgnore
    @Override
    public String getRoleId() {
        return getRoleOid();
    }
}
