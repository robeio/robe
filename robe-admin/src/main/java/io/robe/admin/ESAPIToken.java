package io.robe.admin;

import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import io.robe.common.exception.RobeRuntimeException;
import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.ValidationException;

import java.util.Collections;
import java.util.Map;
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

    @Override
    public void setUserAccountName(String userAccountName) {
        try {
            super.setUserAccountName(userAccountName);
        } catch (ValidationException e) {
            throw new RobeRuntimeException(e);
        }
    }

    @Override
    public void addAttributes(Map<String, String> attrs) {
        try {
            super.addAttributes(attrs);
        } catch (ValidationException e) {
            throw new RobeRuntimeException(e);
        }
    }

    @Override
    public void setAttribute(String name, String value) {
        try {
            super.setAttribute(name, value);
        } catch (ValidationException e) {
            throw new RobeRuntimeException(e);
        }
    }
}
