package io.robe.auth.impl.tokenbased;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import io.robe.auth.IsToken;
import io.robe.auth.TokenWrapper;
import io.robe.auth.entry.PermissionEntry;
import io.robe.auth.entry.RoleEntry;
import io.robe.auth.entry.ServiceEntry;
import io.robe.auth.entry.UserEntry;
import io.robe.auth.store.ServiceStore;
import io.robe.auth.store.UserStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Authenticator implementation for token based authentication.
 */
public class TokenBasedAuthenticator implements Authenticator<String, IsToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBasedAuthenticator.class);

    final ServiceStore serviceStore;
    final UserStore userStore;

    /**
     * Creates an instance of TokenBasedAuthenticator with the store classes.
     *
     * @param userStore    Store for getting user.
     * @param serviceStore Store for getting service info.
     */
    public TokenBasedAuthenticator(UserStore userStore, ServiceStore serviceStore) {
        this.userStore = userStore;
        this.serviceStore = serviceStore;
    }

    /**
     * Creates {@link com.google.common.base.Optional} {@link io.robe.auth.Credentials} instance from provided tokenString
     *
     * @param tokenString tokenString to decode.
     * @return Optional instance of a {@link io.robe.auth.Credentials} which created from tokenString
     * @throws AuthenticationException
     */
    @Override
    public Optional<IsToken> authenticate(String tokenString) throws AuthenticationException {

        try {
            if (tokenString == null) {
                return Optional.absent();
            }

            // Decode tokenString and get user
            IsToken token = TokenWrapper.createToken(tokenString);

            Optional<UserEntry> user = (Optional<UserEntry>) userStore.findByUsername(token.getUserAccountName());
            if (!user.isPresent())
                return Optional.absent();

            // If user exists and active than check Service Permissions for authorization controls
            if (user.get().isActive()) {
                HashSet<String> permissions = new HashSet<String>();
                Set<PermissionEntry> rolePermissions = new HashSet<PermissionEntry>();
                //If user role is a group than add sub role permissions to group
                getAllRolePermissions(user.get().getRole(), rolePermissions);

                for (PermissionEntry permission : rolePermissions) {
                    if (permission.getType().equals(PermissionEntry.Type.SERVICE)) {
                        Optional<? extends ServiceEntry> service = serviceStore.findByCode(permission.getRestrictedItemId());
                        if (service.isPresent())
                            permissions.add(service.get().getPath() + ":" + service.get().getMethod());
                    }
                }
                // Create credentials with user info and permission list
              token.setPermissions(Collections.unmodifiableSet(permissions));
                return Optional.fromNullable(token);
            }
        } catch (InvocationTargetException e) {
            LOGGER.error(tokenString, e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(tokenString, e);
        } catch (InstantiationException e) {
            LOGGER.error(tokenString,e);
        } catch (IllegalAccessException e) {
            LOGGER.error(tokenString, e);
        }
        return Optional.absent();

    }




    /**
     * Fill  permission list  with role and sub-role permissions recursively.
     *
     * @param parent          Role to traverse.
     * @param rolePermissions list of all permissions of the given role.
     */
    private void getAllRolePermissions(RoleEntry parent, Set<PermissionEntry> rolePermissions) {
        rolePermissions.addAll(parent.getPermissions());
        for (RoleEntry role : parent.getRoles()) {
            getAllRolePermissions(role, rolePermissions);
        }
    }
}


