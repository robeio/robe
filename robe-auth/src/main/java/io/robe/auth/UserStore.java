package io.robe.auth;

import com.google.common.base.Optional;

public interface UserStore {
    Optional<? extends UserEntry> findByEmail(String email);
}
