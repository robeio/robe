package io.robe.auth.store;


import com.google.common.base.Optional;
import io.robe.auth.entry.ServiceEntry;

public interface ServiceStore {
    Optional<? extends ServiceEntry> findByCode(String id);
}
