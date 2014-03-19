package io.robe.admin;

import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;

import java.util.Collections;
import java.util.Set;

public class ESAPIToken extends CryptoToken implements IsToken<ESAPIToken>,Credentials {

    private Set<String> permissions;

    public ESAPIToken(){
        super();
    }

    public ESAPIToken(String token) throws EncryptionException {
        super(token);
    }

    @Override
    public void setPermissions(Set<String> permissions) {
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public String getUsername() {
        return getUserAccountName();
    }

}
