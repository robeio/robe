package io.robe.auth.data.store;

import io.robe.auth.data.entry.RoleGroupEntry;

import java.util.Set;

public interface RoleGroupStore {
    Set<? extends RoleGroupEntry> findByGroupId(String groupOid);
}
