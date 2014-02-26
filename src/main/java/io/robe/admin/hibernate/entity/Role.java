package io.robe.admin.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Role extends BaseEntity<Role> {

    @Column(length = 20)
    private String code;

    @Column(length = 50)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<Permission> permissions = new HashSet<Permission>();

    @ManyToMany(targetEntity = Role.class)
    @JoinTable(name = "Role_Group", joinColumns = @JoinColumn(name = "groupOid", referencedColumnName = "oid")
            , inverseJoinColumns = @JoinColumn(name = "roleOid", referencedColumnName = "oid"))
    private Set<Role> roles;

    @JsonIgnore
    @ManyToMany(targetEntity = Role.class)
    @JoinTable(name = "Role_Group", joinColumns = @JoinColumn(name = "roleOid", referencedColumnName = "oid")
            , inverseJoinColumns = @JoinColumn(name = "groupOid", referencedColumnName = "oid"))
    private Set<Role> groups;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;

    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }


    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(HashSet<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getGroups() {
        return groups;
    }

    public void setGroups(Set<Role> groups) {
        this.groups = groups;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
