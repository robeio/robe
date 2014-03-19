package io.robe.auth;

import java.util.Set;


/**
 * Basic Credential model for Auth operations. Secure and lightweight injectable model.
 */
public interface Credentials  {

    public Set<String> getPermissions();


    String getUsername();
}

