package io.robe.auth;

import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;

import java.util.Collections;
import java.util.Set;

public class SampleToken extends CryptoToken implements IsToken<SampleToken>,Credentials {

    private Set<String> permissions;

    public SampleToken(){
        super();
    }

    public SampleToken (String token) throws EncryptionException {
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
