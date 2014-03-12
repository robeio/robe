package io.robe.auth.entry;

public interface PermissionEntry {
    public String getRestrictedItemId();

    public enum Type {
        SERVICE,
        MENU;
    }
    Type getType();
}
