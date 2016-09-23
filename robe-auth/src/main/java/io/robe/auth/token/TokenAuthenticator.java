package io.robe.auth.token;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.data.entry.*;
import io.robe.auth.data.store.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Authenticator implementation for token based authentication.
 */
public class TokenAuthenticator implements Authenticator<String, Token> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticator.class);

    private final ServiceStore serviceStore;
    private final UserStore userStore;
    private final RoleStore roleStore;
    private final PermissionStore permissionStore;
    private final RoleGroupStore roleGroupStore;

    /**
     * Creates an instance of TokenBasedAuthenticator with the store classes.
     *
     * @param userStore    Store for getting user.
     * @param serviceStore Store for getting service info.
     */
    public TokenAuthenticator(UserStore userStore, ServiceStore serviceStore, RoleStore roleStore, PermissionStore permissionStore, RoleGroupStore roleGroupStore) {
        this.userStore = userStore;
        this.serviceStore = serviceStore;
        this.roleStore = roleStore;
        this.permissionStore = permissionStore;
        this.roleGroupStore = roleGroupStore;
    }

    /**
     * Creates {@link Optional} {@link io.robe.auth.Credentials} instance from provided tokenString
     *
     * @param tokenString tokenString to decode.
     * @return Optional instance of a {@link io.robe.auth.Credentials} which created from tokenString
     * @throws AuthenticationException
     */
    @Override
    public Optional<Token> authenticate(String tokenString) throws AuthenticationException {
        tokenString = tokenString.replaceAll("\"", "");
        LOGGER.debug("Authenticating from database:  " + tokenString);
        try {
            // Decode tokenString and get user
            Token token = TokenManager.getInstance().createToken(tokenString);

            Optional<UserEntry> user = (Optional<UserEntry>) userStore.findByUsername(token.getUsername());
            if (!user.isPresent()) {
                LOGGER.warn("User is not available: " + tokenString);
                return Optional.empty();
            }
            // If user exists and active than check Service Permissions for authorization controls
            if (user.get().isActive()) {

                if (token.getPermissions() == null) {
                    LOGGER.debug("Loading Permissions from DB: " + tokenString);
                    Set<String> permissions = new HashSet<String>();
                    Set<PermissionEntry> rolePermissions = new HashSet<PermissionEntry>();

                    //If user role is a group than add sub role permissions to group

                    Optional<RoleEntry> role = (Optional<RoleEntry>) roleStore.findByRoleId(user.get().getRoleId());

                    getAllRolePermissions(role.get(), rolePermissions);

                    for (PermissionEntry permission : rolePermissions) {
                        if (permission.getType().equals(PermissionEntry.Type.SERVICE)) {
                            Optional<? extends ServiceEntry> service = serviceStore.findByCode(permission.getRestrictedItemId());
                            if (service.isPresent()) {
                                permissions.add(service.get().getPath() + ":" + service.get().getMethod());
                            }
                        }
                    }
                    // Create credentials with user info and permission list
                    token.setPermissions(Collections.unmodifiableSet(permissions));
                } else {
                    LOGGER.debug("Loading Permissions from Cache: " + tokenString);
                }

                //Set Token to the thread local for future access.
                TokenManager.setCurrentLoginToken(token);

                return Optional.ofNullable(token);
            }
        } catch (Exception e) {
            LOGGER.error(tokenString, e);
        }
        return Optional.empty();

    }


    /**
     * Fill  permission list  with role and sub-role permissions recursively.
     *
     * @param parent          Role to traverse.
     * @param rolePermissions list of all permissions of the given role.
     */
    private void getAllRolePermissions(RoleEntry parent, Set<PermissionEntry> rolePermissions) {
        rolePermissions.addAll(permissionStore.findByRoleId(parent.getId()));
        Set<RoleGroupEntry> roleGroupEntries = (Set<RoleGroupEntry>) roleGroupStore.findByGroupId(parent.getId());
        for (RoleGroupEntry entry : roleGroupEntries) {
            Optional<RoleEntry> role = (Optional<RoleEntry>) roleStore.findByRoleId(entry.getRoleId());
            getAllRolePermissions(role.get(), rolePermissions);
        }
    }


}


