package io.robe.auth.data.store;

import io.robe.auth.data.entry.PermissionEntry;

import java.util.Set;

public interface PermissionStore {
    Set<? extends PermissionEntry> findByRoleId(String id);
}
