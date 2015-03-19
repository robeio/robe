package io.robe.auth.data.entry;

public interface UserEntry {
    boolean isActive();

    String getUserId();

    String getUsername();

    String getPassword();

    RoleEntry getRole();

}
