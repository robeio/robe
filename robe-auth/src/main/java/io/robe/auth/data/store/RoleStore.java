package io.robe.auth.data.store;

import io.robe.auth.data.entry.RoleEntry;

import java.util.Optional;

public interface RoleStore {
    Optional<? extends RoleEntry> findByRoleId(String id);
}
