package io.robe.hibernate.test.entity;


public class UserDTO {

    private String email;


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private String roleOidName;

    public void setRoleOidName(String roleOidName) {
        this.roleOidName = roleOidName;
    }

    public String getRoleOidName() {
        return roleOidName;
    }
}
