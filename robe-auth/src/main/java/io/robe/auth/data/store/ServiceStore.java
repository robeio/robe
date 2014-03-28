package io.robe.auth.data.store;


import com.google.common.base.Optional;
import io.robe.auth.data.entry.ServiceEntry;

public interface ServiceStore {
    Optional<? extends ServiceEntry> findByCode(String id);
}
