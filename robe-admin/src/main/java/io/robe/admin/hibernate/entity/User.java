package io.robe.admin.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.robe.auth.data.entry.UserEntry;
import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class User extends BaseEntity implements UserEntry {

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int failCount = 0;

    @Column
    private Date lastLoginTime;

    @Column
    private Date lastLogoutTime;

    @Column(length = 32)
    private String roleOid;

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
}
