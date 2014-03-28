package io.robe.auth.data.entry;

public interface UserEntry {
    boolean isActive();

    String getUsername();

    String getPassword();

    RoleEntry getRole();

}
