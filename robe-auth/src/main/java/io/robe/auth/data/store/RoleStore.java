package io.robe.auth.data.store;

import com.google.common.base.Optional;
import io.robe.auth.data.entry.RoleEntry;

public interface RoleStore {
    Optional<? extends RoleEntry> findByRoleId(String id);
}
