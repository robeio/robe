package io.robe.hibernate.test.entity;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.Transient;

public class UserDTO extends BaseEntity {

    private String email;

    private String name;

    private String surname;

    private String password;

    private String roleName;

    @Transient
    private String exampleField;

    public void setExampleField(String exampleField) {
        this.exampleField = exampleField;
    }

    public String getExampleField() {
        return exampleField;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
