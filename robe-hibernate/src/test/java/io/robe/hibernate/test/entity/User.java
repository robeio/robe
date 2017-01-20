package io.robe.hibernate.test.entity;

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
public class User extends BaseEntity {

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

    @SearchFrom(entity = Role.class, filter = "name")
    @Length(min = 32, max = 32)
    @NotEmpty
    @Column(length = 32)
    private String roleOid;

    @Transient
    @Relation(name = "roleOid.name")
    private String roleName;


    @Transient
    private String exampleTransient;

    public User(){

    }

    public User(String email, String name, String surname, String password, boolean active, int failCount, Date lastLoginTime, Date lastLogoutTime, String roleOid, String exampleTransient) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.active = active;
        this.failCount = failCount;
        this.lastLoginTime = lastLoginTime;
        this.lastLogoutTime = lastLogoutTime;
        this.roleOid = roleOid;
        this.exampleTransient = exampleTransient;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }

    public void setExampleTransient(String exampleTransient) {
        this.exampleTransient = exampleTransient;
    }

    public String getExampleTransient() {
        return exampleTransient;
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
                ", exampleTransient='" + exampleTransient + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (isActive() != user.isActive()) return false;
        if (getFailCount() != user.getFailCount()) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        if (getSurname() != null ? !getSurname().equals(user.getSurname()) : user.getSurname() != null) return false;
        if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null)
            return false;
        if (getLastLoginTime() != null ? !getLastLoginTime().equals(user.getLastLoginTime()) : user.getLastLoginTime() != null)
            return false;
        if (getLastLogoutTime() != null ? !getLastLogoutTime().equals(user.getLastLogoutTime()) : user.getLastLogoutTime() != null)
            return false;
        if (getRoleOid() != null ? !getRoleOid().equals(user.getRoleOid()) : user.getRoleOid() != null) return false;
        return getExampleTransient() != null ? getExampleTransient().equals(user.getExampleTransient()) : user.getExampleTransient() == null;
    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSurname() != null ? getSurname().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (isActive() ? 1 : 0);
        result = 31 * result + getFailCount();
        result = 31 * result + (getLastLoginTime() != null ? getLastLoginTime().hashCode() : 0);
        result = 31 * result + (getLastLogoutTime() != null ? getLastLogoutTime().hashCode() : 0);
        result = 31 * result + (getRoleOid() != null ? getRoleOid().hashCode() : 0);
        result = 31 * result + (getExampleTransient() != null ? getExampleTransient().hashCode() : 0);
        return result;
    }
}
