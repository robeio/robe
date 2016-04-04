package io.robe.admin.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.auth.data.entry.RoleEntry;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Role extends BaseEntity implements RoleEntry {

    @Column(length = 32, unique = true,nullable = false)
    private String code;

    @Column(length = 50,nullable = false)
    private String name;

    @Transient
    private List<Role> roles = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return getOid();
    }
}
