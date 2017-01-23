package io.robe.hibernate.test.entity;


import io.robe.common.service.search.Relation;

public class UserDTO {
    @Relation(name = "name")
    private String userName;
    private String email;
    @Relation(name = "roleOid.name")
    private String roleOidName;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setRoleOidName(String roleOidName) {
        this.roleOidName = roleOidName;
    }

    public String getRoleOidName() {
        return roleOidName;
    }
}
