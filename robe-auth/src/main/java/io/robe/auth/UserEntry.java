package io.robe.auth;

public interface UserEntry {
    boolean isActive();

    String getUsername();

    String getPassword();

     RoleEntry getRole();

}
