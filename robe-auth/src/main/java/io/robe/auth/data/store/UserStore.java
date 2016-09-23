package io.robe.auth.data.store;

import io.robe.auth.data.entry.UserEntry;

import java.util.Optional;

public interface UserStore {
    Optional<? extends UserEntry> findByUsername(String username);

    Optional<? extends UserEntry> changePassword(String username ,String newPassword);
}
