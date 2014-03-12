package io.robe.auth.entry;

public interface UserEntry {
    boolean isActive();

    String getUsername();

    String getPassword();

    RoleEntry getRole();

}
