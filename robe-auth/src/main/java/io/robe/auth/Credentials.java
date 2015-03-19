package io.robe.auth;

/**
 * Basic Credential model for Auth operations. Secure and lightweight injectable model.
 */
public interface Credentials  {

    String getUserId();
    String getUsername();
}

