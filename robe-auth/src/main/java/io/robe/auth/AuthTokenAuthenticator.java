package io.robe.auth;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.ValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Authenticator implemented for auth-token
 */
public class AuthTokenAuthenticator implements Authenticator<String, Credentials> {


    ServiceStore serviceStore;
    UserStore userStore;

    /**
     * Creates an instance of AuthTokenAuthenticator with the dao classes.
     *
     * @param userStore    User DAO for authenticating user
     * @param serviceStore Service DAO for permission list
     */
    @Inject
    public AuthTokenAuthenticator(UserStore userStore, ServiceStore serviceStore) {
        this.userStore = userStore;
        this.serviceStore = serviceStore;
    }

    /**
     * Creates {@link com.google.common.base.Optional} {@link io.robe.auth.Credentials} instance from provided auth-token
     *
     * @param token Auth-Token to decode.
     * @return Optional instance of a {@link io.robe.auth.Credentials} which created from token
     * @throws AuthenticationException
     */
    @Override
    public Optional<Credentials> authenticate(String token) throws AuthenticationException {

        try {
            if (token == null) {
                return Optional.absent();
            }
            // All ok decode token and check user credentials
            CryptoToken cryptoToken = new CryptoToken(token);
            Optional<UserEntry> user = (Optional<UserEntry>) userStore.findByEmail(cryptoToken.getUserAccountName());
            if (!user.isPresent())
                return Optional.absent();

            // If access granted collect users Service Permissions for authorization controls
            if (user.get().isActive() && user.get().getUsername().equals(cryptoToken.getUserAccountName())) {
                HashSet<String> permissions = new HashSet<String>();
                Set<PermissionEntry> rolePermissions = new HashSet<PermissionEntry>();
                //add sub role permissions
                getAllRolePermissions(user.get().getRole(), rolePermissions);

                for (PermissionEntry permission : rolePermissions) {
                    if (permission.getType().equals(PermissionEntry.Type.SERVICE)) {
                        ServiceEntry service = serviceStore.findById(permission.getRestrictedItemId());
                        if (service != null)
                            permissions.add(service.getPath() + ":" + service.getMethod());
                    }
                }
                Credentials credentials = new Credentials(user.get().getUsername(), user.get().getPassword(), Collections.unmodifiableSet(permissions));
                return Optional.fromNullable(credentials);
            }
        } catch (EncryptionException e) {
            e.printStackTrace();
        }
        return Optional.absent();

    }

    /**
     * Creates an access token with the given {@link io.robe.auth.Credentials}
     *
     * @param credentials {@link io.robe.auth.Credentials} instance provided to create token.
     * @return {@link org.owasp.esapi.crypto.CryptoToken} instance with 10min. expiration.
     * @throws ValidationException
     */
    protected static CryptoToken createToken(Credentials credentials) throws ValidationException {
        CryptoToken cryptoToken = new CryptoToken();
        cryptoToken.setUserAccountName(credentials.getUsername());
        cryptoToken.setExpiration(600);
        return cryptoToken;
    }


    private void getAllRolePermissions(RoleEntry parent, Set<PermissionEntry> rolePermissions) {
        rolePermissions.addAll(parent.getPermissions());
        for (RoleEntry role : parent.getRoles()) {
            getAllRolePermissions(role, rolePermissions);
        }
    }
}


