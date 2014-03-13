package io.robe.auth.store;

import com.google.common.base.Optional;
import io.robe.auth.entry.UserEntry;

public interface UserStore {
    Optional<? extends UserEntry> findByUsername(String username);

    Optional<? extends UserEntry> changePassword(String username ,String newPassword);
}
