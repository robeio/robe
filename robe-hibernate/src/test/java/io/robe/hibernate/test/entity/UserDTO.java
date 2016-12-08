package io.robe.hibernate.test.entity;


public class UserDTO {

    private String email;
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
