package io.robe.auth;

public interface PermissionEntry {
    public String getRestrictedItemId();

    public enum Type {
        SERVICE,
        MENU;
    }
    Type getType();
}
