package io.robe.auth.tokenbased;

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
     * Sets the expirationdate by adding duration to current time
     *
     * @param durationInSeconds
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
     * @return
     */
    String getAttributesHash();

    /**
     * String representation of the token
     *
     * @return
     * @throws Exception
     */
    String getTokenString() throws Exception;

    /**
     * Max age of the token
     *
     * @return
     */
    int getMaxAge();

    /**
     * Set permissions to token
     *
     * @param permissions
     */
    void setPermissions(Set<String> permissions);

    /**
     * Gets permissions
     *
     * @return
     */
    Set<String> getPermissions();

}
