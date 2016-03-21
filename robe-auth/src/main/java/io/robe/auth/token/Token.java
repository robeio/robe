package io.robe.auth.token;

import io.robe.auth.Credentials;

import java.util.Date;
import java.util.Set;

public interface Token extends Credentials {

    /**
     * Returns owner username of the  token
     *
     * @return username
     */
    String getUserId();

    /**
     * Returns owner username of the  token
     *
     * @return username
     */
    String getUsername();

    /**
     * Returns the status of token expiration.
     *
     * @return true if token expired.
     */
    boolean isExpired();

    /**
     * Sets the expiration date by adding duration to current time
     *
     * @param durationInSeconds duration for expiration in seconds
     */
    void setExpiration(int durationInSeconds);

    /**
     * Returns the expiration date for token
     *
     * @return expiration date
     */
    Date getExpirationDate();

    /**
     * Returns the calculated hash of attributes.
     *
     * @return hash of attributes which taken from http headers
     */
    String getAttributesHash();

    /**
     * String representation of the token
     *
     * @return returns the token as string
     * @throws Exception throws if it is not possible to calculate the stoken string
     */
    String getTokenString() throws Exception;

    /**
     * Max age of the token
     *
     * @return max age of cookie
     */
    int getMaxAge();

    /**
     * Set permissions to token
     *
     * @param permissions permission list of the current user
     */
    void setPermissions(Set<String> permissions);

    /**
     * Gets permissions
     *
     * @return permission list of the current user
     */
    Set<String> getPermissions();

}
