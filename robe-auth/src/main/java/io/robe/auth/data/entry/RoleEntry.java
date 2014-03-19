package io.robe.auth.data.entry;

import java.util.Collection;
import java.util.Set;

public interface RoleEntry  {
    Collection<? extends PermissionEntry> getPermissions();

    Set<? extends RoleEntry> getRoles();
}
