package io.robe.admin.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.auth.data.entry.UserEntry;
import io.robe.common.service.search.Relation;
import io.robe.common.service.search.SearchFrom;
import io.robe.common.service.search.SearchIgnore;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table
public class User extends BaseEntity implements UserEntry {

    @Length(min = 5, max = 50)
    @NotEmpty
    @Pattern(regexp = "\\S+@\\S+\\.\\S+")
    @Column(unique = true, length = 50)
    private String email;

    @Length(min = 3, max = 50)
    @NotEmpty
    @Column(length = 50, nullable = false)
    private String name;


    @NotEmpty
    @Length(min = 2, max = 50)
    @Column(length = 50, nullable = false)
    private String surname;

    @SearchIgnore
    @Length(min = 64, max = 64)
    @NotEmpty
    @Column(length = 64, nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int failCount = 0;

    @Column
    private Date lastLoginTime;

    @Column
    private Date lastLogoutTime;

    @SearchIgnore
    @SearchFrom(entity = Role.class, filter = "name", id = "oid")
    @Length(min = 32, max = 32)
    @NotEmpty
    @Column(length = 32)
    private String roleOid;

    @Relation(name = "roleOid.name")
    @Transient
    private String roleName;

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleOid() {
        return roleOid;
    }

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }

    @JsonIgnore
    @Override
    public String getRoleId() {
        return getRoleOid();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonIgnore
    @Override
    public String getUserId() {
        return getOid();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getEmail();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", failCount=" + failCount +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLogoutTime=" + lastLogoutTime +
                ", roleOid='" + roleOid + '\'' +
                '}';
    }
}
