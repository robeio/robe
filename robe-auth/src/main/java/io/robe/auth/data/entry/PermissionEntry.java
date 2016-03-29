package io.robe.auth.data.entry;

public interface PermissionEntry {
    String getRestrictedItemId();

    Type getType();

    public enum Type {
        SERVICE,
        MENU
    }
}
