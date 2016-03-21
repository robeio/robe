package io.robe.auth;

import java.security.Principal;

/**
 * Basic Credential model for Auth operations. Secure and lightweight injectable model.
 */
public interface Credentials extends Principal {

    String getUserId();
    String getUsername();
}

