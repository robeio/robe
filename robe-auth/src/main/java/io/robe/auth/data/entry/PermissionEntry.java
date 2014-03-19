package io.robe.auth.data.entry;

public interface PermissionEntry {
    public String getRestrictedItemId();

    public enum Type {
        SERVICE,
        MENU;
    }
    Type getType();
}
