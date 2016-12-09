package io.robe.auth.data.store;


import io.robe.auth.data.entry.ServiceEntry;

import java.util.Optional;

public interface ServiceStore {
    Optional<? extends ServiceEntry> findByCode(String id);
}
